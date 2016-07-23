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

import java.net.URI;

/**
 * Created by ozsie on 22/07/16.
 */
public class DocType extends XmlElement<String> {

    private String rootElement;
    private String name;
    private URI location;
    private DocTypeAvailability availability;
    private String internalSubset;

    public DocTypeAvailability getAvailability() {
        return availability;
    }

    public void setAvailability(DocTypeAvailability availability) {
        this.availability = availability;
    }

    public String getRootElement() {
        return rootElement;
    }

    public void setRootElement(String rootElement) {
        this.rootElement = rootElement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getLocation() {
        return location;
    }

    public void setLocation(URI location) {
        this.location = location;
    }

    public String getInternalSubset() {
        return internalSubset;
    }

    public void setInternalSubset(String internalSubset) {
        this.internalSubset = internalSubset;
    }

    @Override
    public String toFormattedString() {
        String retVal = "<!DOCTYPE " + rootElement;
        if (availability != null) {
            retVal += " " + availability;
        }
        switch (availability) {
            case PUBLIC:
                retVal += " \"" + name + "\"";
            case SYSTEM:
                retVal += " \"" + location + "\"";
        }
        if (internalSubset != null && !internalSubset.isEmpty()) {
            retVal += "\n\t[";
            retVal += "\n\t\t" + internalSubset;
            retVal += "\n\t]";
            retVal += "\n>";
        } else {
            retVal += ">";
        }
        return retVal;
    }

    @Override
    public String toFormattedString(String tabs) {
        return toFormattedString();
    }

    @Override
    public String toString() {
        String retVal = "<!DOCTYPE " + rootElement;
        if (availability != null) {
            retVal += " " + availability;
        }
        switch (availability) {
            case PUBLIC:
                retVal += " \"" + name + "\"";
            case SYSTEM:
                retVal += " \"" + location + "\"";
        }
        if (internalSubset != null && !internalSubset.isEmpty()) {
            retVal += " [ " + internalSubset + " ] ";
        }
        retVal += ">";
        return retVal;
    }
}
