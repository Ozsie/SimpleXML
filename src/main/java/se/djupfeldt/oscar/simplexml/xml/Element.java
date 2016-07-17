package se.djupfeldt.oscar.simplexml.xml;

/**
 * Created by ozsie on 14/07/16.
 */
public abstract class Element<T> {
    private T content;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public abstract String toFormattedString();
    public abstract String toFormattedString(String tabs);
}
