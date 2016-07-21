/*
 * SimpleXML is a simple XML parser.  It reads an XML file or String and returns a Document object.
 * Copyright (C) 2016 Oscar Djupfeldt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.djupfeldt.oscar.simplexml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.djupfeldt.oscar.simplexml.handlers.*;
import se.djupfeldt.oscar.simplexml.util.StringInputStream;
import se.djupfeldt.oscar.simplexml.xml.Comment;
import se.djupfeldt.oscar.simplexml.xml.Document;
import se.djupfeldt.oscar.simplexml.xml.Node;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by osdjup on 2016-07-14.
 */
public class XmlReader {
    private static Logger LOG = LoggerFactory.getLogger(XmlReader.class);
    boolean forceStringAttributes = false;
    boolean forceStringContent = false;

    CommentHandler commentHandler;
    NodeHandler nodeHandler;
    PrologHandler prologHandler;
    DocTypeHandler docTypeHandler;

    Node root;

    Boolean foundProlog;
    Boolean foundDocType;

    public XmlReader() {
        commentHandler = new CommentHandler();
        prologHandler = new PrologHandler();
        docTypeHandler = new DocTypeHandler();
        nodeHandler = new NodeHandler(forceStringAttributes, forceStringContent);
    }

    public XmlReader(boolean forceStringAttributes, boolean forceStringContent) {
        commentHandler = new CommentHandler();
        prologHandler = new PrologHandler();
        docTypeHandler = new DocTypeHandler();
        nodeHandler = new NodeHandler(forceStringAttributes, forceStringContent);

        this.forceStringAttributes = forceStringAttributes;
        this.forceStringContent = forceStringContent;
    }

    public Document read(File file) throws XmlParseException, IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        Document doc = parseXmlText(bis);
        bis.close();
        return doc;
    }

    public Document read(String xml) throws XmlParseException, IOException {
        if (xml == null || xml.isEmpty()) {
            throw new XmlParseException("No text to parse");
        }
        StringInputStream sis = new StringInputStream(xml);
        Document doc = parseXmlText(sis);
        sis.close();
        return doc;
    }

    public Document parseXmlText(InputStream inputStream) throws XmlParseException, IOException {

        Document document = new Document();
        root = null;
        Node currentParent = null;

        foundProlog = false;
        foundDocType = false;

        while (true) {
            inputStream.mark(0);
            int currentInt = inputStream.read();
            Character current = (char) currentInt;
            if (currentInt < 0) {
                LOG.debug("Reached end of document");
                break;
            }
            Character next = (char) inputStream.read();
            if (next == '<') {
                inputStream.reset();
                inputStream.read();
                continue;
            }
            if (current == '<') {
                if (next == '?' && !foundProlog) {
                    foundProlog = prologHandler.lookForProlog(inputStream, document);
                    continue;
                } else if (next == '?' && foundProlog) {
                    throw new XmlParseException("Found more than one XML prolog");
                } else if (next == '!') {
                    getSpecialTag(inputStream, document);
                } else if(next == '/') {
                    currentParent = getClosingTag(currentParent, inputStream, current, next);
                } else {
                    currentParent = getOpeningTag(document, currentParent, inputStream);
                }
            }
        }

        return document;
    }

    void getSpecialTag(InputStream inputStream, Document document) throws XmlParseException, IOException {
        char next = (char)inputStream.read();
        char nextNext = (char)inputStream.read();
        if (next == 'D' && !foundDocType) {
            foundDocType = docTypeHandler.lookForDocType(inputStream, document);
        } else if (next == '-' && nextNext == '-') {
            Comment comment = commentHandler.readComment(inputStream);
            LOG.debug("Found Comment: {}", comment);
        } else if (next == '[' && nextNext == 'C') {
            LOG.debug("Found CDATA");
        }
    }

    Node getOpeningTag(Document document, Node currentParent, InputStream inputStream) throws XmlParseException, IOException {
        inputStream.reset();
        Node node = nodeHandler.readTag(inputStream);
        if (root == null) {
            currentParent = node;
            root = node;
            document.setRoot(root);
        } else {
            node.setParent(currentParent);
            if (node.isClosed() && node.getChildren().size() > 0) {
                currentParent.getChildren().addAll(node.getChildren());
                node.getChildren().clear();
            }
            currentParent.getChildren().add(node);
            if (!node.isClosed()) {
                currentParent = node;
            }
        }

        return currentParent;
    }

    Node getClosingTag(Node currentParent, InputStream sr, char current, char next) throws IOException, XmlParseException {
        int currentInt;
        String tag = "" + current + next;
        while (true) {
            currentInt = sr.read();
            current = (char) currentInt;
            if (currentInt == -1) {
                throw new XmlParseException("Could not read end of closing tag: " + tag);
            }
            tag += current;
            if (current == '>') {
                break;
            }
        }
        tag = tag.substring(2, tag.length() - 1);
        if (!tag.equals(currentParent.getName())) {
            throw new XmlParseException("Tag mismatch: " + currentParent.getName() + ", " + tag);
        }
        if (currentParent.getParent() != null) {
            currentParent = currentParent.getParent();
        }
        return currentParent;
    }


}
