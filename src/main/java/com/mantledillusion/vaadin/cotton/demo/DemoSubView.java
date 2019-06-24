package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.injection.hura.core.Bus;
import com.mantledillusion.injection.hura.core.annotation.event.Subscribe;
import com.mantledillusion.injection.hura.core.annotation.injection.Inject;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyPressEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;

public class DemoSubView extends Div {

    private TextField field;

    public DemoSubView(@Inject Bus bus) {
        this.field = new TextField();
        this.field.addKeyPressListener(Key.ENTER, bus::publish);

        add(this.field);
    }

    @Subscribe
    private void receive(KeyPressEvent event) {
        this.field.setValue(((TextField) event.getSource()).getValue());
    }
}
