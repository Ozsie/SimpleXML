package se.djupfeldt.oscar.simplexml.handlers;

import se.djupfeldt.oscar.simplexml.XmlParseException;
import se.djupfeldt.oscar.simplexml.xml.Comment;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by ozsie on 16/07/16.
 */
public class CommentHandler {

    public Comment readComment(StringReader sr) throws IOException, XmlParseException {
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
