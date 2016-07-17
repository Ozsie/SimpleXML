package se.djupfeldt.oscar.simplexml.handlers;

import se.djupfeldt.oscar.simplexml.XmlParseException;
import se.djupfeldt.oscar.simplexml.xml.Document;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by ozsie on 17/07/16.
 */
public class DocTypeHandler {

    public boolean lookForDocType(StringReader sr, String tag, Document document) throws IOException, XmlParseException {
        sr.reset();
        for (int i = 0; i < 9; i++) {
            tag += (char) sr.read();
        }
        if (tag.equals("<!DOCTYPE")) {
            boolean subset = false;
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

                if (current == '[') {
                    subset = true;
                }
                if (current == ']') {
                    subset = false;
                }
                if (current == '>' && !subset) {
                    document.setDocType(tag);
                    return true;
                }
            }
        }

        return false;
    }
}
