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
 * Created by osdjup on 2016-07-14.
 */
public class Node<T> extends XmlElement<T> {
    public static final int CONTENT_LENGTH_BEFORE_NL = 50;
    private Node parent;
    private List<XmlElement> children;
    private List<Comment> comments;
    private List<Attribute> attributes;
    private String name;
    private boolean closed;

    public Node() {
        children = new ArrayList<>();
        attributes = new ArrayList<>();
        comments = new ArrayList<>();
        name = "";
    }

    public Node(String name, T content) {
        children = new ArrayList<>();
        attributes = new ArrayList<>();
        comments = new ArrayList<>();
        this.setContent(content);
        this.name = name;
    }

    public Node getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public List<XmlElement> getChildren() {
        return children;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean hasChild(String name) {
        for (XmlElement child : children) {
            if (child instanceof Node) {
                if (((Node) child).getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String toFormattedString(String tabs) {
        String retVal =  tabs + "<" + name;
        if (attributes.size() > 0) {
            for (Attribute a : attributes) {
                retVal += " " + a.toString();
            }
        }
        if (!closed) {
            retVal += ">";
            if (getContent() != null) {
                if (getContent() instanceof String) {
                    String c = (String) getContent();
                    if (c.length() > CONTENT_LENGTH_BEFORE_NL) {
                        retVal += "\n\t" + tabs;
                    }
                }

                retVal += getContent();
                if (getContent() instanceof String) {
                    String c = (String) getContent();
                    if (c.length() > CONTENT_LENGTH_BEFORE_NL) {
                        retVal += "\n" + tabs + "</" + name + ">";
                    } else {
                        retVal += "</" + name + ">";
                    }
                } else {
                    retVal += "</" + name + ">";
                }
            } else {
                if (children.size() > 0){
                    for (XmlElement child : children) {
                        retVal += "\n" + child.toFormattedString(tabs + "\t");
                    }
                    retVal += "\n" + tabs + "</" + name + ">";
                } else {
                    retVal += "</" + name + ">";
                }
            }
        } else {
            retVal += "/>";
        }

        return retVal;
    }

    public String toFormattedString() {
        return toFormattedString("");
    }

    @Override
    public String toString() {
        String retVal =  "<" + name;
        if (attributes.size() > 0) {
            for (Attribute a : attributes) {
                retVal += " " + a.toString();
            }
        }
        if (!closed) {
            retVal += ">";
            if (getContent() != null) {
                String content = String.valueOf(getContent());
                for (int i = 0; i < comments.size(); i++) {
                    content = content.replace("&" + i + ";", comments.get(i).toString());
                }
                retVal += content;
            } else {
                if (children.size() > 0){
                    for (XmlElement child : children) {
                        retVal += child.toString();
                    }
                }
            }
            retVal += "</" + name + ">";
        } else {
            retVal += "/>";
        }

        return retVal;
    }
}
