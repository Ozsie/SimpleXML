package se.djupfeldt.oscar.simplexml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by osdjup on 2016-07-14.
 */
public class XmlReader {
    Logger LOG = LoggerFactory.getLogger(XmlReader.class);
    boolean forceStringAttributes = false;
    boolean forceStringContent = false;
    public XmlReader() {

    }

    public XmlReader(boolean forceStringAttributes, boolean forceStringContent) {
        this.forceStringAttributes = forceStringAttributes;
        this.forceStringContent = forceStringContent;
    }

    public Document read(String filePath) throws XmlParseException {
        String fileContent = null;
        return parseXmlText(fileContent);
    }

    public Document parseXmlText(String fileContent) throws XmlParseException {
        if (fileContent == null || fileContent.isEmpty()) {
            throw new XmlParseException("No text to parse");
        }

        Node root = null;
        Node currentParent = null;
        try {
            StringReader sr = new StringReader(fileContent);
            int current = 0;
            while ((current = sr.read()) != -1) {
                char currentChar = (char)current;
                if (currentChar == '<') {
                    sr.mark(0);
                    char next = (char) sr.read();
                    if (next == '/') {
                        if (currentParent.getParent() != null) {
                            currentParent = currentParent.getParent();
                        }
                    } else if (next == '!') {
                        LOG.debug("Found comment");
                        Comment comment = getComment(sr);
                        currentParent.getComments().add(comment);
                    } else if (next == '?') {
                        LOG.debug("Found header");
                    } else {
                        sr.reset();
                        if (root == null) {
                            root = readTag(sr);
                            currentParent = root;
                        } else {
                            Node node = readTag(sr);
                            node.setParent(currentParent);
                            if (node.isClosed() && node.getChildren().size() > 0) {
                                currentParent.getChildren().addAll(node.getChildren());
                                node.getChildren().clear();
                            }
                            currentParent.getChildren().add(node);
                            if (!node.isClosed() && node.getContent() == null) {
                                currentParent = node;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {

        }
        return new Document(root);
    }

    private Node readTag(StringReader sr) throws IOException {
        String tag = "";
        int current = 0;
        while ((current = sr.read()) != '>') {
            if (current == -1) {
                break;
            }
            tag += (char) current;
        }

        tag = tag.trim();

        boolean closed = tag.endsWith("/");
        if (closed) {
            tag = tag.substring(0, tag.length() - 1);
        }

        String[] parts = tag.split(" ");
        String name = parts[0];

        Node node;
        if (closed) {
            node = new Node<String>();
        } else {
            node = getContent(sr);
        }

        getAttributes(node, parts);
        node.setClosed(closed);
        node.setName(name);

        return node;
    }

    private Node getContent(StringReader sr) throws IOException {
        int current;
        sr.mark(0);
        String trim = "";
        do  {
            trim += (char)sr.read();
        } while (trim.trim().isEmpty());
        trim = trim.trim();
        char next = (char) sr.read();
        if (!trim.equals("<")) {
            sr.reset();
            return getNodeContent(sr);
        } else if (trim.equals("<") && next == '!') {
            sr.reset();
            Comment comment = getComment(sr);
            Node node = getNodeContent(sr);
            node.getChildren().add(comment);
            LOG.debug("FOUND COMMENT: {}", comment.getContent());
            return node;
        } else {
            sr.reset();
        }
        return new Node<>();
    }

    private Node getNodeContent(StringReader sr) throws IOException {
        int current;
        String text = "";
        List<Comment> comments = new ArrayList<>();
        char currentChar;
        while ((current = sr.read()) != -1) {
            currentChar = (char) current;
            sr.mark(0);
            if (current == '<') {
                if (sr.read() == '!') {
                    LOG.debug("FOUND COMMENT");
                    sr.reset();
                    Comment comment = getComment(sr);
                    comment.setContent(comment.getContent().replaceFirst("!--", "").trim());
                    comments.add(comment);
                    text += "&" + comments.indexOf(comment) + ";";
                } else {
                    sr.reset();
                    break;
                }
            } else {
                text += (char) current;
            }
        }

        text = text.trim();
        if (text.isEmpty()) {
            sr.reset();
            return new Node<>();
        }
        if (forceStringContent) {
            Node<String> node = new Node();
            node.setContent(text);
            node.getComments().addAll(comments);
            return node;
        } else if (isBoolean(text)) {
            Node<Boolean> node = new Node<>();
            node.setContent(Boolean.parseBoolean(text));
            node.getComments().addAll(comments);
            return node;
        } else if (isInteger(text)) {
            Node<Long> node = new Node<>();
            node.setContent(Long.parseLong(text));
            node.getComments().addAll(comments);
            return node;
        } else if (isDouble(text)) {
            Node<Double> node = new Node<>();
            node.setContent(Double.parseDouble(text));
            node.getComments().addAll(comments);
            return node;
        } else {
            Node<String> node = new Node<>();
            node.setContent(text);
            node.getComments().addAll(comments);
            return node;
        }
    }

    private Comment getComment(StringReader sr) throws IOException {
        int current;
        String content = "";
        Comment comment = new Comment();
        while ((current = sr.read()) != '>') {
            if (current == -1) {
                break;
            }
            content += (char) current;
        }
        content = content.replaceFirst("<!--", "");
        content = content.substring(0, content.length() - 2);
        content = content.trim();
        comment.setContent(content);
        return comment;
    }

    private void getAttributes(Node node, String[] parts) {
        for (int i = 1; i < parts.length; i++) {
            String attr = parts[i].trim();
            String[] attrParts = attr.split("=");
            String value = attrParts[1].substring(1, attrParts[1].length() - 1);
            if (forceStringAttributes) {
                Attribute<String> attribute = new Attribute<>(attrParts[0], value);
                node.getAttributes().add(attribute);
            } else if (isBoolean(value)) {
                Attribute<Boolean> attribute = new Attribute<>(attrParts[0], Boolean.parseBoolean(value));
                node.getAttributes().add(attribute);
            } else if (isInteger(value)) {
                Attribute<Long> attribute = new Attribute<>(attrParts[0], Long.parseLong(value));
                node.getAttributes().add(attribute);
            } else if (isDouble(value)) {
                Attribute<Double> attribute = new Attribute<>(attrParts[0], Double.parseDouble(value));
                node.getAttributes().add(attribute);
            } else {
                Attribute<String> attribute = new Attribute<>(attrParts[0], value);
                node.getAttributes().add(attribute);
            }
        }
    }

    private boolean isBoolean(String value) {
        return (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"));
    }

    private boolean isInteger(String value) {
        try {
            Long.parseLong(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
