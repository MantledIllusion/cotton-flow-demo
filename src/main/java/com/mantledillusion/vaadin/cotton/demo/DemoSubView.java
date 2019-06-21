package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.injection.hura.core.annotation.injection.Inject;
import com.mantledillusion.vaadin.cotton.event.EventBusSubscriber;
import com.mantledillusion.vaadin.cotton.event.Subscribe;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;

public class DemoSubView extends Div {

    public static class TextFieldEvent extends EventBusSubscriber.BusEvent {

        private final String value;

        public TextFieldEvent(String value) {
            this.value = value;
        }
    }

    public static class TextFieldSubscriber extends EventBusSubscriber {

        private final TextField field = new TextField();

        public TextFieldSubscriber() {
            this.field.addKeyPressListener(Key.ENTER, event -> this.dispatch(new TextFieldEvent(this.field.getValue())));
        }

        @Subscribe(isSelfObservant = false)
        private void receive(TextFieldEvent event) {
            this.field.setValue(event.value);
        }
    }

    public DemoSubView(@Inject TextFieldSubscriber subscriber) {
        add(subscriber.field);
    }
}
