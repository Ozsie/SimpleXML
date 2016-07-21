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
import se.djupfeldt.oscar.simplexml.xml.Comment;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ozsie on 16/07/16.
 */
public class CommentHandler {

    public Comment readComment(InputStream sr) throws IOException, XmlParseException {
        sr.reset();
        String commentString = "";
        for (int i = 0; i < 3; i++) {
            commentString += (char) sr.read();
        }
        while (true) {
            int currentInt = sr.read();
            char current = (char) currentInt;
            if (currentInt == -1) {
                throw new XmlParseException("Unclosed comment");
            }
            String currentStr = "" + current;
            if (currentStr.trim().isEmpty()) {
                current = ' ';
                if (commentString.endsWith(" ")) {
                    continue;
                }
            }
            commentString += current;
            if (current == '-') {
                sr.mark(0);
                char oneAhead = (char) sr.read();
                char twoAhead = (char) sr.read();
                if (oneAhead == '-') {
                    if (twoAhead == '>') {
                        commentString += oneAhead + "" + twoAhead;
                        break;
                    } else {
                        throw new XmlParseException("Comments may not contain '--'");
                    }
                }
                sr.reset();
            }
        }
        Comment comment = new Comment();
        comment.setContent(commentString.substring(5, commentString.length() - 4));
        return  comment;
    }
}
