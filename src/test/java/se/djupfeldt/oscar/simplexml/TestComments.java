package se.djupfeldt.oscar.simplexml;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import se.djupfeldt.oscar.simplexml.xml.Document;

import java.io.IOException;

/**
 * Created by ozsie on 16/07/16.
 */
public class TestComments {
    XmlReader reader;
    @Before
    public void setup() {
        reader = new XmlReader();
    }

    @Test
    public void testCommentInsideContent() {
        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ><root><!-- comment -->TEXT<!-- comment -->HELLO</root";
        try {
            Document doc = reader.parseXmlText(text);
            Assert.assertNotNull(doc.getRoot());
            Assert.assertEquals(2, doc.getRoot().getChildren().size());
            Assert.assertEquals(2, doc.getRoot().getComments().size());
            Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ><root><!-- comment -->TEXT<!-- comment -->HELLO</root>", doc.toString());
        } catch (XmlParseException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCommentInsideTag() {
        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ><root <!-- comment --> />";
        try {
            Document doc = reader.parseXmlText(text);
            Assert.assertNotNull(doc.getRoot());
            Assert.assertEquals(0, doc.getRoot().getChildren().size());
            Assert.assertEquals(1, doc.getRoot().getComments().size());
            Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ><root/>", doc.toString());
        } catch (XmlParseException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCommentInsideComment() {
        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ><root <!-- com<!-- comment -->ment --> />";
        try {
            Document doc = reader.parseXmlText(text);
            Assert.assertNotNull(doc.getRoot());
            Assert.assertEquals(0, doc.getRoot().getChildren().size());
            Assert.assertEquals(1, doc.getRoot().getComments().size());
            Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ><root/>", doc.toString());
        } catch (XmlParseException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCommentInsideAttribute() {
        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ><root attr=\"BU<!-- comment -->RP\" />";
        try {
            Document doc = reader.parseXmlText(text);
            Assert.assertNotNull(doc.getRoot());
            Assert.assertEquals(0, doc.getRoot().getChildren().size());
            Assert.assertEquals(1, doc.getRoot().getComments().size());
            Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ><root attr=\"BURP\"/>", doc.toString());
        } catch (XmlParseException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCommentAsChild() {

    }
}
