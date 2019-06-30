package com.mantledillusion.vaadin.cotton.demo;

public class ApiEvent {

    private final Object source;
    private final String text;

    public ApiEvent(Object source,String text) {
        this.source = source;
        this.text = text;
    }

    public Object getSource() {
        return source;
    }

    public String getText() {
        return text;
    }
}
