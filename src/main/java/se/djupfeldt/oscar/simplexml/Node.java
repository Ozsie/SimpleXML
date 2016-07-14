package se.djupfeldt.oscar.simplexml;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by osdjup on 2016-07-14.
 */
public class Node {
    private List<Node> children;
    private List<Attribute> attributes;
    private String content;
    private String name;

    public Node() {
        children = new ArrayList<>();
        attributes = new ArrayList<>();
        content = "";
        name = "";
    }

    public Node(String name, String content) {
        children = new ArrayList<>();
        attributes = new ArrayList<>();
        this.content = content;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public List<Node> getChildren() {
        return children;
    }

    public String getContent() {
        return content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        String retVal =  "<" + name;
        retVal += name;
        if (attributes.size() > 0) {
            retVal += " ";
            for (Attribute a : attributes) {
                retVal += a.getName() + "=\"" + a.getValue() + "\"";
            }
        }
        if (children.size() > 0 || !content.isEmpty()) {
            retVal += "/>";
        } else {
            retVal += ">";
        }

        return retVal;
    }
}
