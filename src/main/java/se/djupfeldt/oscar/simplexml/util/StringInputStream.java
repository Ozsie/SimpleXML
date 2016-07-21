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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

/**
 * Created by ozsie on 19/07/16.
 */
public class StringInputStream extends InputStream {
    private static Logger LOG = LoggerFactory.getLogger(StringInputStream.class);
    private StringReader sr;
    public StringInputStream(String string) {
        sr = new StringReader(string);
    }

    @Override
    public int read() throws IOException {
        return sr.read();
    }

    @Override
    public synchronized void mark(int readlimit) {
        try {
            sr.mark(readlimit);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    @Override
    public boolean markSupported() {
        return sr.markSupported();
    }

    @Override
    public long skip(long n) throws IOException {
        return sr.skip(n);
    }

    @Override
    public synchronized void reset() throws IOException {
        sr.reset();}

    @Override
    public int read(byte[] b) throws IOException {
        throw new IOException("Read into byte[] not supported.");
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        throw new IOException("Read into byte[] not supported.");
    }

    @Override
    public int available() throws IOException {
        throw new IOException("Available not supported.");
    }

    @Override
    public void close() throws IOException {
        sr.close();
    }

    public boolean ready() throws IOException {
        return sr.ready();
    }

    public int read(char cbuf[], int off, int len) throws IOException {
        return sr.read(cbuf, off, len);
    }

    public int read(char cbuf[]) throws IOException {
        return sr.read(cbuf);
    }


}
