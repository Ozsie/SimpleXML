package se.djupfeldt.oscar.simplexml.handlers;

import se.djupfeldt.oscar.simplexml.XmlParseException;
import se.djupfeldt.oscar.simplexml.xml.Document;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by ozsie on 16/07/16.
 */
public class PrologHandler {

    public void parseProlog(String tag, Document document) {
        String[] parts = tag.split(" ");
        for (int i = 0; i < parts.length; i++) {
            String[] attr = parts[i].trim().split("=");
            if (attr.length == 2) {
                String name = attr[0];
                String value = attr[1].replace("\"", "");
                switch (name) {
                    case "version":
                        document.setXmlVersion(value);
                        break;
                    case "encoding":
                        document.setEncoding(value);
                        break;
                    case "standalone":
                        document.setStandalone(value.equalsIgnoreCase("yes") ? true : false);
                        break;
                }
            }
        }
    }

    public boolean lookForProlog(StringReader sr, String tag, Document document) throws IOException, XmlParseException {
        sr.reset();
        for (int i = 0; i < 5; i++) {
            tag += (char)sr.read();
        }
        if (tag.equals("<?xml")) {
            while (true) {
                int currentInt = sr.read();
                char current = (char) currentInt;
                if (currentInt == -1) {
                    throw new XmlParseException("Could not find end of XML prolog");
                }
                String currentStr = "" + current;
                if (currentStr.trim().isEmpty()) {
                    current = ' ';
                    if (tag.endsWith(" ")) {
                        continue;
                    }
                }
                tag += current;
                if (current == '>') {
                    parseProlog(tag, document);
                    return true;
                }
            }
        }
        return false;
    }
}
