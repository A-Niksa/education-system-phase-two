package client.controller;

public class StringObjectMap {
    private String string;
    private Object object;

    public StringObjectMap(String string, Object object) {
        this.string = string;
        this.object = object;
    }

    public String getString() {
        return string;
    }

    public Object getObject() {
        return object;
    }
}
