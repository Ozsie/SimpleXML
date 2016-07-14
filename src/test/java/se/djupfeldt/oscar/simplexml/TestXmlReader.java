package se.djupfeldt.oscar.simplexml;

import java.io.FileReader;

/**
 * Created by osdjup on 2016-07-14.
 */
public class TestXmlReader {

    @org.junit.Test
    public void testParseXmlText() {
        String text = "<root a=\"a\" b=\"b\"><child1 c=\"c\"/><child2>hej hej<child2><child3><child4/>child3/></root>";
        XmlReader reader = new XmlReader();
        try {
            Node root = reader.parseXmlText(text);
        } catch (XmlParseException e) {
            e.printStackTrace();
        }
    }
}
