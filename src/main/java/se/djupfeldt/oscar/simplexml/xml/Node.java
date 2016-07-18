package se.djupfeldt.oscar.simplexml.xml;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by osdjup on 2016-07-14.
 */
public class Node<T> extends Element<T> {
    private Node parent;
    private List<Element> children;
    private List<Comment> comments;
    private List<Attribute> attributes;
    private T content;
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
        this.content = content;
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

    public List<Element> getChildren() {
        return children;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public T getContent() {
        return content;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean hasChild(String name) {
        for (Element child : children) {
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
            if (content != null) {
                retVal += "\n\t" + tabs;
                String content = (String)this.content;
                for (int i = 0; i < comments.size(); i++) {
                    content = content.replace("&" + i + ";", comments.get(i).toString());
                }
                retVal += content;
            } else {
                if (children.size() > 0){
                    for (Element child : children) {
                        retVal += "\n" + child.toFormattedString(tabs + "\t");
                    }
                }
            }
            retVal += "\n" + tabs + "</" + name + ">";
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
            if (content != null) {
                String content = (String)this.content;
                for (int i = 0; i < comments.size(); i++) {
                    content = content.replace("&" + i + ";", comments.get(i).toString());
                }
                retVal += content;
            } else {
                if (children.size() > 0){
                    for (Element child : children) {
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
