package se.djupfeldt.oscar.simplexml.xml;

/**
 * Created by ozsie on 14/07/16.
 */
public class Comment extends Element<String> {

    @Override
    public String toString() {
        return "<!-- " + getContent() + " -->";
    }

    @Override
    public String toFormattedString() {
        return toFormattedString("");
    }

    @Override
    public String toFormattedString(String tabs) {
        return tabs + toString();
    }
}
