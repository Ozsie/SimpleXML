package se.djupfeldt.oscar.simplexml;

/**
 * Created by osdjup on 2016-07-14.
 */
public class Attribute<T> {
    private String name;
    private T value;

    public Attribute() {}

    public Attribute(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if (value instanceof String) {
            return name + "=\"" + value + "\"";
        } else {
            return name + "=(" + value.getClass().getSimpleName() + ")\"" + value + "\"";
        }
    }
}
