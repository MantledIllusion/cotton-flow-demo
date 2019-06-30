package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.injection.hura.core.Bus;
import com.mantledillusion.injection.hura.core.annotation.event.Subscribe;
import com.mantledillusion.injection.hura.core.annotation.injection.Inject;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;

public class ViewA extends Div {

    private TextField textField;

    public ViewA(@Inject Bus bus) {
        this.textField = new TextField("View A");
        this.textField.addKeyPressListener(Key.ENTER, event -> bus.publish(
                new ApiEvent(ViewA.this, this.textField.getValue())));
        add(this.textField);
    }

    @Subscribe
    private void receive(ApiEvent event) {
        if (event.getSource() != this) {
            this.textField.setValue(event.getSource().getClass().getSimpleName()+" says: "+event.getText());
        }
    }
}
