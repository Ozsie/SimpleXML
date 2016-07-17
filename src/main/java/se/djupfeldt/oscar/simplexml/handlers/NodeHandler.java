package se.djupfeldt.oscar.simplexml.handlers;

import se.djupfeldt.oscar.simplexml.xml.Attribute;
import se.djupfeldt.oscar.simplexml.xml.Node;
import se.djupfeldt.oscar.simplexml.Util;
import se.djupfeldt.oscar.simplexml.XmlParseException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ozsie on 16/07/16.
 */
public class NodeHandler {
    boolean forceStringAttributes = false;

    public NodeHandler(boolean forceStringAttributes) {
        this.forceStringAttributes = forceStringAttributes;
    }

    public Node readTag(StringReader sr) throws IOException, XmlParseException {
        // Skip <
        sr.read();
        String name = readTagName(sr);
        List<Attribute> attributes = new ArrayList<>();
        if (name.endsWith(">")) {
            name = name.substring(0, name.length() - 1);
        } else {
            attributes = getAttributes(sr, name);
        }

        Node node = new Node();
        node.setName(name);
        node.getAttributes().addAll(attributes);

        return node;
    }

    private List<Attribute> getAttributes(StringReader sr, String tag) throws IOException, XmlParseException {
        String attributeString = readAttributes(sr, tag);
        List<String> attrParts = splitAttributeString(attributeString);
        List<Attribute> attributes = new ArrayList<>();
        for (String attribute : attrParts) {
            String[] pair = attribute.split("=");
            if (pair.length < 2) {
                throw new XmlParseException("Found attribute without value, " + pair[0] + " in tag " + tag);
            } else {
                String name = pair[0];
                String value = "";
                for (int i = 1; i < pair.length; i++) {
                    value += "=" + pair[i];
                }
                value = value.trim();
                value = value.substring(1, value.length() - 1);
                Attribute attrib;
                if (forceStringAttributes) {
                    attrib = new Attribute<>(name, value);
                } else if (Util.isLong(value)) {
                    attrib = new Attribute<>(name, Long.parseLong(value));
                } else if (Util.isDouble(value)) {
                    attrib = new Attribute<>(name, Double.parseDouble(value));
                } else if (Util.isBoolean(value)) {
                    attrib = new Attribute<>(name, Boolean.parseBoolean(value));
                } else {
                    attrib = new Attribute<>(name, value);
                }
                attributes.add(attrib);
            }

        }
        return attributes;
    }

    private List<String> splitAttributeString(String attributeString) throws IOException {
        StringReader ar = new StringReader(attributeString);
        List<String> attrParts = new ArrayList<>();
        boolean readingAttrValue = false;
        String part = "";
        while (true) {
            int currentInt = ar.read();
            char current = (char) currentInt;
            if (currentInt == -1) {
                break;
            }
            if (current == '"') {
                if (readingAttrValue) {
                    attrParts.add(part + current);
                    part = "";
                }
                readingAttrValue = !readingAttrValue;
            }
            if (current == ' ' && !readingAttrValue) {
                part = "";
            }
            part += current;
        }
        return attrParts;
    }

    String readAttributes(StringReader sr, String tag) throws IOException, XmlParseException {
        String attributes = "";
        boolean readingAttribValue = false;
        while (true) {
            int currentInt = sr.read();
            char current = (char) currentInt;
            if (current == '"') {
                readingAttribValue = !readingAttribValue;
            }
            if (currentInt == -1) {
                throw new XmlParseException("Could not find end of tag " + tag);
            }
            if (current == '<' && !readingAttribValue) {
                throw new XmlParseException("Unexpected character '<' in tag " + tag);
            }
            if (current == '>' && !readingAttribValue) {
                break;
            }
            if (current == '/' && !readingAttribValue) {
                break;
            }
            attributes += current;
        }
        return attributes.trim();
    }

    String readTagName(StringReader sr) throws IOException, XmlParseException {
        String tag = "";
        while (true) {
            int currentInt = sr.read();
            char current = (char) currentInt;
            if (currentInt == -1) {
                throw new XmlParseException("Could not find end of tag " + tag);
            }
            String currentStr = "" + current;
            if (currentStr.trim().isEmpty()) {
                break;
            }

            tag += current;
            if (current == '>') {
                break;
            }
        }

        return tag;
    }
}
