package se.djupfeldt.oscar.simplexml;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.djupfeldt.oscar.simplexml.xml.Attribute;
import se.djupfeldt.oscar.simplexml.xml.Comment;
import se.djupfeldt.oscar.simplexml.xml.Document;
import se.djupfeldt.oscar.simplexml.xml.Node;

import java.io.IOException;

/**
 * Created by osdjup on 2016-07-14.
 */
public class TestXmlReader {
    static XmlReader reader;
    static Document doc;
    static String wellformedFlatXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" [ <!-- an internal subset can be embedded here --> ] ><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>title</title><meta content=\"text/xhtml=banana\"/></head><body class=\"false\"><div><br/><p>aasd</p><div class=\"a b c d\"><span><![CDATA[ <<>>]]></span><span></span><span>Hej <![CDATA[ <<>>]]></span><span>Hej <![CDATA[ <<!-- Apa -->>]]></span></div></div></body></html>";
    @BeforeClass
    public static void setup() {
        reader = new XmlReader();
        String filePath = "src/test/resources/wellformed.xml";

        try {
            doc = reader.read(filePath);
        } catch (IOException | XmlParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParseXmlText() {
        System.out.println(doc.toFormattedString());
        System.out.println(doc.toString());
        Assert.assertEquals(wellformedFlatXml, doc.toString());
    }

    @Test
    public void testProlog() {
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.getXmlVersion());
        Assert.assertEquals("1.0", doc.getXmlVersion());
        Assert.assertNotNull(doc.getEncoding());
        Assert.assertEquals("UTF-8", doc.getEncoding());
        Assert.assertNotNull(doc.isStandalone());
        Assert.assertEquals(Boolean.TRUE, doc.isStandalone());
    }

    @Test
    public void testDoctype() {
        Assert.assertNotNull(doc);
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\" [ <!-- an internal subset can be embedded here --> ] >", doc.getDocType());
    }

    @Test
    public void testStructure() {
        Node root = doc.getRoot();
        Assert.assertNotNull(root);
        Assert.assertEquals(root.getName(), "html");
        Assert.assertEquals(root.getChildren().size(), 2);
        Node head = (Node) root.getChildren().get(0);
        Assert.assertEquals(head.getName(), "head");
        Assert.assertEquals(head.getChildren().size(), 2);
        Node title = (Node) head.getChildren().get(0);
        Assert.assertEquals(title.getName(), "title");
        Assert.assertEquals(title.getContent(), "title");
        Assert.assertEquals(root.getAttributes().size(), 1);
        Attribute xmlns = (Attribute) root.getAttributes().get(0);
        Assert.assertEquals(xmlns.getName(), "xmlns");
        Assert.assertEquals(xmlns.getValue(), "http://www.w3.org/1999/xhtml");
        Assert.assertEquals(root.getComments().size(), 1);
        Comment rootComment = (Comment) root.getComments().get(0);
        Assert.assertEquals(rootComment.getContent(), "comment 2");
    }
}

/*
<? header >
<root a=(Long)"7" b=(Double)"5.6">
	<child1 c=(Boolean)"false"/>
	<child4>
		<child6/>
        <!-- comment 1 -->
		<child7>
		    <!-- comment 2 -->
			aasd
		</child7>
	</child4>
	<child2(Long)>
		15
	</child2>
	<child3>
		<child4/>
		<child4/>
		<child5 d="d">
			apa<!-- comment 3 -->apa
		</child5>
	</child3>
	<child4/>
</root>
 */