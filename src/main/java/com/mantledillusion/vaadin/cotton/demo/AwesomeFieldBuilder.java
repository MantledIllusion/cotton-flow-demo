package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.vaadin.cotton.component.builders.AbstractComponentBuilder;
import com.mantledillusion.vaadin.cotton.component.mixin.HasSizeBuilder;

public class AwesomeFieldBuilder extends AbstractComponentBuilder<AwesomeField, AwesomeFieldBuilder>
		implements HasSizeBuilder<AwesomeField, AwesomeFieldBuilder> {

    @Override
    public AwesomeField instantiate() {
        return new AwesomeField();
    }

    public AwesomeFieldBuilder setAwesomeText(String text) {
        configure(field -> field.setAwesomeText(text));
        return this;
    }
}
