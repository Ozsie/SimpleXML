package se.djupfeldt.oscar.simplexml.xml;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ozsie on 14/07/16.
 */
public class Document {
    private Node root;
    private String xmlVersion;
    private String encoding;
    private Boolean standalone;
    private String docType;

    public Document() {}

    public Document(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getXmlVersion() {
        return xmlVersion;
    }

    public Boolean isStandalone() {
        return standalone;
    }

    public Boolean getStandalone() {
        return standalone;
    }

    public String getDocType() {
        return docType;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setStandalone(Boolean standalone) {
        this.standalone = standalone;
    }

    public void setXmlVersion(String xmlVersion) {
        this.xmlVersion = xmlVersion;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public List<Node> getNodesByName(String name) {
        return getNodesByName(name, root);
    }

    public List<Node> getNodesByName(String name, Node startingPoint) {
        List<Node> nodes = new ArrayList<>();
        for (Object obj : startingPoint.getChildren()) {
            if (obj instanceof Node) {
                Node node = (Node) obj;
                if (node.getName().equalsIgnoreCase(name)) {
                    nodes.add(node);
                }
                nodes.addAll(getNodesByName(name, node));
            }
        }
        return nodes;
    }

    @Override
    public String toString() {
        String retVal = "<?xml version=\"" + xmlVersion + "\" encoding=\"" + encoding + "\" standalone=\"";
        retVal += (standalone ? "yes" : "no") + "\" >";
        retVal += docType;
        retVal += root.toString();
        return retVal;
    }

    public String toFormattedString() {
        String retVal = "<?xml version=\"" + xmlVersion + "\" encoding=\"" + encoding + "\" standalone=\"";
        retVal += (standalone ? "yes" : "no") + "\" >\n";
        if (docType != null) {
            retVal += docType + "\n";
        }
        retVal += root.toFormattedString();
        return retVal;
    }
}
