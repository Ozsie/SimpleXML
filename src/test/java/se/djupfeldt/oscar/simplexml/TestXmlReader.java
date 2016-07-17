package se.djupfeldt.oscar.simplexml;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import se.djupfeldt.oscar.simplexml.xml.Document;

import java.io.IOException;

/**
 * Created by osdjup on 2016-07-14.
 */
public class TestXmlReader {
    XmlReader reader;
    Document doc;
    @Before
    public void setup() {
        reader = new XmlReader();
        String filePath = "src/test/resources/wellformed.xml";
        try {
            doc = reader.read(filePath);
        } catch (IOException | XmlParseException e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void testParseXmlText() {
        System.out.println(doc.toFormattedString());
        System.out.println(doc.toString());
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