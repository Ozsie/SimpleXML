package se.djupfeldt.oscar.simplexml;

import java.io.FileReader;
import java.util.List;

/**
 * Created by osdjup on 2016-07-14.
 */
public class TestXmlReader {

    @org.junit.Test
    public void testParseXmlText() {
        String text = "<? header >\n" +
                "<root a=\"7\" b=\"5.6\">\n" +
                "<child8><!-- comment 5 -->aas<!-- comment 6 -->d</child8>" +
                "\t<child1 c=\"false\"/>\n" +
                "\t<child4>\n" +
                "\t\t<child6/>\n" +
                "        <!-- comment -- 1 -->\n" +
                "\t\t<child7>\n" +
                "\t\t    <!-- comment 2 -->\n" +
                "\t\t\taasd\n" +
                "\t\t</child7>\n" +
                "\t</child4>\n" +
                "\t<child2>\n" +
                "\t\t15\n" +
                "\t</child2>\n" +
                "\t<child3>\n" +
                "\t\t<child4/>\n" +
                "\t\t<child4/>\n" +
                "\t\t<child5 d=\"d\">\n" +
                "\t\t\tapa<!-- comment 3 -->apa<!-- comment 4 -->apa\n" +
                "\t\t</child5>\n" +
                "\t</child3>\n" +
                "\t<child4/>\n" +
                "</root>";
        XmlReader reader = new XmlReader();
        try {
            Document doc = reader.parseXmlText(text);
            System.out.println(doc.toFormattedString());
        } catch (XmlParseException e) {
            e.printStackTrace();
        }
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