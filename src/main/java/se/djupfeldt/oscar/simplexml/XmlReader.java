package se.djupfeldt.oscar.simplexml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.djupfeldt.oscar.simplexml.handlers.*;
import se.djupfeldt.oscar.simplexml.xml.Comment;
import se.djupfeldt.oscar.simplexml.xml.Document;
import se.djupfeldt.oscar.simplexml.xml.Node;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

/**
 * Created by osdjup on 2016-07-14.
 */
public class XmlReader {
    Logger LOG = LoggerFactory.getLogger(XmlReader.class);
    boolean forceStringAttributes = false;
    boolean forceStringContent = false;

    CommentHandler commentHandler;
    NodeHandler nodeHandler;
    PrologHandler prologHandler;
    DocTypeHandler docTypeHandler;

    public XmlReader() {
        commentHandler = new CommentHandler();
        prologHandler = new PrologHandler();
        docTypeHandler = new DocTypeHandler();
        nodeHandler = new NodeHandler(forceStringAttributes);
    }

    public XmlReader(boolean forceStringAttributes, boolean forceStringContent) {
        commentHandler = new CommentHandler();
        prologHandler = new PrologHandler();
        docTypeHandler = new DocTypeHandler();
        nodeHandler = new NodeHandler(forceStringAttributes);

        this.forceStringAttributes = forceStringAttributes;
        this.forceStringContent = forceStringContent;
    }

    public Document read(String filePath, Charset charset) throws XmlParseException, IOException {
        String fileContent = Util.readFile(filePath, charset);
        return parseXmlText(fileContent);
    }

    public Document read(String filePath) throws XmlParseException, IOException {
        String fileContent = Util.readFile(filePath, Charset.defaultCharset());
        return parseXmlText(fileContent);
    }

    public Document parseXmlText(String fileContent) throws XmlParseException, IOException {
        if (fileContent == null || fileContent.isEmpty()) {
            throw new XmlParseException("No text to parse");
        }

        Document document = new Document();
        Node root = null;
        Node currentParent = null;
        Node previousReadNode = null;

        boolean foundProlog = false;
        boolean foundDocType = false;

        StringReader sr = new StringReader(fileContent);
        String total = "";
        while (true) {
            sr.mark(0);
            int currentInt = sr.read();
            char current = (char) currentInt;
            if (currentInt < 0) {
                LOG.debug("Reached end of document");
                break;
            }
            char next = (char) sr.read();
            if (next == '<') {
                sr.reset();
                sr.read();
                sr.mark(0);
                currentInt = sr.read();
                current = (char) currentInt;
                next = (char) sr.read();
            }
            if (current == '<') {
                String tag = "";
                if (next == '?' && !foundProlog) {
                    LOG.debug("Found PROLOG");
                    foundProlog = prologHandler.lookForProlog(sr, tag, document);
                    continue;
                } else if (next == '?' && foundProlog) {
                    throw new XmlParseException("Found more than one XML prolog");
                } else if (next == '!') {
                    next = (char)sr.read();
                    if (next == 'D' && !foundDocType) {
                        LOG.debug("Found DOCTYPE");
                        foundDocType = docTypeHandler.lookForDocType(sr, tag, document);
                        continue;
                    }
                    if (next == '-' && sr.read() == '-') {
                        Comment comment = commentHandler.readComment(sr);
                        LOG.debug("Found Comment: {}", comment);
                    }
                } else {
                    LOG.debug("Found tag");
                    sr.reset();
                    Node node = nodeHandler.readTag(sr);
                    if (root == null) {
                        root = node;
                        currentParent = node;
                        document.setRoot(root);
                    }
                }
            }
        }

        return document;
    }





}
