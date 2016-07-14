package se.djupfeldt.oscar.simplexml;

import java.io.FileReader;
import java.io.StringReader;

/**
 * Created by osdjup on 2016-07-14.
 */
public class XmlReader {
    public Node read(String filePath) throws XmlParseException {

        String fileContent = null;
        return parseXmlText(fileContent);
    }

    public Node parseXmlText(String fileContent) throws XmlParseException {
        if (fileContent == null || fileContent.isEmpty()) {
            throw new XmlParseException("No text to parse");
        }
        //String processed = preProcessString(fileContent);
        String[] parts = fileContent.split("(\\<)");
        for (String str : parts) {
            parseRow(str);
            System.out.println(str);
        }
        Node root = new Node();

        return root;
    }

    public String preProcessString(String fileContent) {
        String processedString = "";
        for (int i = 0; i < fileContent.length(); i++) {
            if (fileContent.charAt(i) == '>') {
                processedString += "\n";
            } else {
                processedString += fileContent.charAt(i);
            }
        }

        return processedString;
    }

    Node parseRow(String str) {
        String[] elements = str.split(" ");
        Node node = new Node();
        node.setName(elements[0]);
        if (str.endsWith("/>")) {
        }

        return node;
    }
}
