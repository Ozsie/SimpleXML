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
        retVal += (standalone ? "yes" : "no") + "\" ?>";
        retVal += docType;
        retVal += root.toString();
        return retVal;
    }

    public String toFormattedString() {
        if ((xmlVersion == null || xmlVersion.isEmpty()) &&
                (encoding == null || encoding.isEmpty()) &&
                standalone == null) {
            return root.toFormattedString();
        }
        String retVal = "<?xml ";
        if (xmlVersion != null && !xmlVersion.isEmpty()) {
            retVal += "version=\"" + xmlVersion + " ";
        }
        if (encoding != null && !encoding.isEmpty()) {
            retVal += "encoding=\"" + encoding + "\" ";
        }
        if (standalone != null) {
            retVal += "standalone=\"" + (standalone ? "yes" : "no") + "\" ";
        }
        retVal += "?>\n";
        if (docType != null) {
            retVal += docType + "\n";
        }
        retVal += root.toFormattedString();
        return retVal;
    }
}
