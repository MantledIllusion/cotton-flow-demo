package com.mantledillusion.vaadin.cotton.demo;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.textfield.TextField;

public class AwesomeField extends Composite<TextField> implements HasSize {

    public AwesomeField() {
        getContent().setLabel("Write something awesome!");
    }

    public void setAwesomeText(String text) {
        getContent().setValue(text);
    }
}
