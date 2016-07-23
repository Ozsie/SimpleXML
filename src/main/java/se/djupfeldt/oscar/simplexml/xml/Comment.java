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

/**
 * Created by ozsie on 14/07/16.
 */
public class Comment extends XmlElement<String> {

    @Override
    public String toString() {
        return "<!-- " + getContent() + " -->";
    }

    @Override
    public String toFormattedString() {
        return toFormattedString("");
    }

    @Override
    public String toFormattedString(String tabs) {
        return tabs + toString();
    }
}
