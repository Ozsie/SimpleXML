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

package se.djupfeldt.oscar.simplexml.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ozsie on 21/07/16.
 */
public class TestUtil {

    @Test
    public void testIsBoolean() {
        String _true = "true";
        String TRUE = "TRUE";
        String tRuE = "tRuE";

        String _false = "false";
        String FALSE = "FALSE";
        String fAlSe = "fAlSe";

        String onePointTwo = "1.2";
        String five = "5";
        String one = "1";
        String monkey = "monkey";

        Assert.assertTrue(Util.isBoolean(_true));
        Assert.assertTrue(Util.isBoolean(TRUE));
        Assert.assertTrue(Util.isBoolean(tRuE));
        Assert.assertTrue(Util.isBoolean(_false));
        Assert.assertTrue(Util.isBoolean(FALSE));
        Assert.assertTrue(Util.isBoolean(fAlSe));
        Assert.assertFalse(Util.isBoolean(onePointTwo));
        Assert.assertFalse(Util.isBoolean(five));
        Assert.assertFalse(Util.isBoolean(one));
        Assert.assertFalse(Util.isBoolean(monkey));
    }

    @Test
    public void testIsLong() {
        String a = "-1000";
        String b = "287593847593845";
        String c = "23";
        String d = "9348";
        String e = "-45345345345";
        String f= "-123123";

        String onePointTwo = "1.2";
        String oneCommaFour = "1,4";
        String _true = "true";
        String _false = "false";

        Assert.assertTrue(Util.isLong(a));
        Assert.assertTrue(Util.isLong(b));
        Assert.assertTrue(Util.isLong(c));
        Assert.assertTrue(Util.isLong(d));
        Assert.assertTrue(Util.isLong(e));
        Assert.assertTrue(Util.isLong(f));
        Assert.assertFalse(Util.isLong(onePointTwo));
        Assert.assertFalse(Util.isLong(oneCommaFour));
        Assert.assertFalse(Util.isLong(_true));
        Assert.assertFalse(Util.isLong(_false));
    }

    @Test
    public void testIsDouble() {
        String a = "-1000.000";
        String b = "287593847593845.23434";
        String c = "23.234";
        String d = "9348.1233";
        String e = "-45345345345.123123";
        String f= "-123123.34545";

        String twelve = "12";
        String fourteen4343 = "144343";
        String _true = "true";
        String _false = "false";

        Assert.assertTrue(Util.isDouble(a));
        Assert.assertTrue(Util.isDouble(b));
        Assert.assertTrue(Util.isDouble(c));
        Assert.assertTrue(Util.isDouble(d));
        Assert.assertTrue(Util.isDouble(e));
        Assert.assertTrue(Util.isDouble(f));
        Assert.assertFalse(Util.isDouble(twelve));
        Assert.assertFalse(Util.isDouble(fourteen4343));
        Assert.assertFalse(Util.isDouble(_true));
        Assert.assertFalse(Util.isDouble(_false));
    }
}
