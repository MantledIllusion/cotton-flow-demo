package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.vaadin.cotton.component.builders.LabelBuilder;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("demo")
public class DemoView extends Div {

    public DemoView() {
        LabelBuilder labelBuilder = new LabelBuilder()
                .setExactWidth(100)
                .setHeightUndefined();

        add(labelBuilder.setValue("A").build(), labelBuilder.setValue("B").build());
    }
}