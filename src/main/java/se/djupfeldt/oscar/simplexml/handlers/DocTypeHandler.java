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
import se.djupfeldt.oscar.simplexml.xml.DocType;
import se.djupfeldt.oscar.simplexml.xml.DocTypeAvailability;
import se.djupfeldt.oscar.simplexml.xml.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by ozsie on 17/07/16.
 */
public class DocTypeHandler {

    public boolean lookForDocType(InputStream sr, Document document) throws IOException, XmlParseException, URISyntaxException {
        String tag = "";
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
                    DocType docType = parseDocType(tag);
                    document.setDocType(docType);
                    return true;
                }
            }
        }

        return false;
    }

    private DocType parseDocType(String docTypeString) throws IOException, XmlParseException, URISyntaxException {
        String tag = "";
        StringReader sr = new StringReader(docTypeString);
        for (int i = 0; i < 9; i++) {
            tag += (char) sr.read();
        }
        if (!tag.equalsIgnoreCase("<!DOCTYPE")) {
            throw new XmlParseException("Malformed doctype.");
        }
        String rootElement = readRootElement(sr);
        DocTypeAvailability availability = readAvailability(sr);
        String name = null;
        URI location = null;
        if (availability != null) {
            switch (availability) {
                case PUBLIC:
                    name = readName(sr);
                case SYSTEM:
                    location = readLocation(sr);
                    break;
            }
        }

        String internalSubset = readInternalSubset(sr);

        DocType docType = new DocType();
        docType.setRootElement(rootElement);
        docType.setAvailability(availability);
        docType.setName(name);
        docType.setLocation(location);
        docType.setInternalSubset(internalSubset);

        return docType;
    }

    private String readRootElement(StringReader sr) throws IOException {
        char current;
        String availability = "";
        //Skip space
        sr.read();
        while ((current = (char) sr.read()) != ' ') {
            availability += current;
        }
        return availability;
    }

    private DocTypeAvailability readAvailability(StringReader sr) throws IOException {
        sr.mark(0);
        char current;
        String availability = "";
        while ((current = (char) sr.read()) != ' ') {
            availability += current;
        }
        try {
            return DocTypeAvailability.valueOf(availability);
        } catch (IllegalArgumentException e) {
            sr.reset();
            return null;
        }
    }

    private String readName(StringReader sr) throws IOException {
        char current;
        String name = "";
        //Skip "
        sr.read();
        while ((current = (char) sr.read()) != '"') {
            name += current;
        }
        return name;
    }

    private URI readLocation(StringReader sr) throws IOException, URISyntaxException {
        char current;
        String location = "";
        //Skip space and "
        sr.read();
        sr.read();
        while ((current = (char) sr.read()) != '"') {
            location += current;
        }
        return new URI(location);
    }

    private String readInternalSubset(StringReader sr) throws IOException, URISyntaxException {
        sr.mark(0);
        char current;
        String internal = "";
        //Skip space
        sr.read();
        if (sr.read() == '[') {
            while ((current = (char) sr.read()) != ']') {
                internal += current;
            }
            return internal.trim();
        }
        sr.reset();
        return null;
    }
}
