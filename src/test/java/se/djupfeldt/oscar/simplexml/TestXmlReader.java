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
                "<root a=(Long)\"7\" b=(Double)\"5.6\">\n" +
                "\t<child1 c=(Boolean)\"false\"/>\n" +
                "\t<child4>\n" +
                "\t\t<child6/>\n" +
                "        <!-- comment -- 1 -->\n" +
                "\t\t<child7>\n" +
                "\t\t    <!-- comment 2 -->\n" +
                "\t\t\taasd\n" +
                "\t\t</child7>\n" +
                "\t</child4>\n" +
                "\t<child2(Long)>\n" +
                "\t\t15\n" +
                "\t</child2>\n" +
                "\t<child3>\n" +
                "\t\t<child4/>\n" +
                "\t\t<child4/>\n" +
                "\t\t<child5 d=\"d\">\n" +
                "\t\t\tapa apa\n" +
                "\t\t</child5>\n" +
                "\t</child3>\n" +
                "\t<child4/>\n" +
                "</root>";
        XmlReader reader = new XmlReader();
        try {
            Document doc = reader.parseXmlText(text);
            System.out.println(doc.toFormattedString());
            List<Node> child4 = doc.getNodesByName("child4");
            for (Node n : child4) {
                System.out.println("--");
                System.out.println(n.toFormattedString());
            }
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
			apa apa
		</child5>
	</child3>
	<child4/>
</root>
 */