package se.djupfeldt.oscar.simplexml;

/**
 * Created by ozsie on 14/07/16.
 */
public abstract class Element<T> {
    T content;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public abstract String toFormattedString();
    public abstract String toFormattedString(String tabs);
}
