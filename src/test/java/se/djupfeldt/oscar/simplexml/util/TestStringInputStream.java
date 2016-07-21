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

import java.io.IOException;

/**
 * Created by ozsie on 19/07/16.
 */
public class TestStringInputStream {

    @Test
    public void testRead() {
        try {
            StringInputStream sis = new StringInputStream("test");
            int current;
            String test = "";
            while ((current = sis.read()) != -1) {
                test += (char) current;
            }
            sis.close();
            Assert.assertEquals("Entire String is read", "test", test);
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void testMarkSupported() {
        try {
            StringInputStream sis = new StringInputStream("test");
            sis.close();
            Assert.assertTrue("Mark should be supported", sis.markSupported());
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void testMarkAndReset() {
        try {
            StringInputStream sis = new StringInputStream("test");
            int current = sis.read();
            sis.mark(0);

            while (sis.read() != -1) {}
            sis.reset();
            current = sis.read();
            sis.close();
            Assert.assertEquals("After mark, read until -1 and reset, read returns correctly", (int)'e', current);
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void testSkip() {
        try {
            StringInputStream sis = new StringInputStream("test");
            long skip = sis.skip(2L);
            int read = sis.read();
            sis.close();
            Assert.assertEquals("sis.skip(2L) should skip two chars", 2, skip);
            Assert.assertEquals("Read after sis.skip(2L) should return 's'", (int)'s', read);
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test(expected = IOException.class)
    public void testReadByteArray() throws IOException {
        byte[] arr = new byte[10];
        StringInputStream sis = new StringInputStream("test");
        sis.read(arr);
    }

    @Test(expected = IOException.class)
    public void testReadByteArrayOffsetLen() throws IOException {
        byte[] arr = new byte[10];
        StringInputStream sis = new StringInputStream("test");
        sis.read(arr, 1, 1);
    }

    @Test
    public void testReady() {
        try {
            StringInputStream sis = new StringInputStream("test");
            Assert.assertTrue(sis.ready());
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test(expected = NullPointerException.class)
    public void testReadyNoString() throws IOException {
        StringInputStream sis = new StringInputStream(null);
        sis.ready();
    }

    @Test(expected = NullPointerException.class)
    public void testReadNoString() throws IOException {
        StringInputStream sis = new StringInputStream(null);
        sis.read();
    }

    @Test
    public void testReadIntoBuf() {
        try {
            StringInputStream sis = new StringInputStream("test");
            char[] buf = new char[4];
            int read = sis.read(buf);
            Assert.assertEquals("Read should return 4 chars read", 4, read);
            Assert.assertEquals('t', buf[0]);
            Assert.assertEquals('e', buf[1]);
            Assert.assertEquals('s', buf[2]);
            Assert.assertEquals('t', buf[3]);
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void testReadIntoBufOffsetLen() {
        try {
            StringInputStream sis = new StringInputStream("test");
            char[] buf = new char[4];
            int read = sis.read(buf, 1, 2);
            Assert.assertEquals("Read should return 2 chars read", 2, read);
            Assert.assertEquals('t', buf[1]);
            Assert.assertEquals('e', buf[2]);
        } catch (IOException e) {
            Assert.fail();
        }
    }
}
