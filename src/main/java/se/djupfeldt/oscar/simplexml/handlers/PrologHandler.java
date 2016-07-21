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

package se.djupfeldt.oscar.simplexml.handlers;

import se.djupfeldt.oscar.simplexml.XmlParseException;
import se.djupfeldt.oscar.simplexml.xml.Document;

import java.io.IOException;
import java.io.InputStream;

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

    public boolean lookForProlog(InputStream sr, Document document) throws IOException, XmlParseException {
        String tag = "";
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
                if (current == '?' && sr.read() == '>') {
                    parseProlog(tag, document);
                    return true;
                }
            }
        }
        return false;
    }
}
