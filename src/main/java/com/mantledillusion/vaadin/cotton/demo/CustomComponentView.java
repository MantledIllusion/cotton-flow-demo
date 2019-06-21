package com.mantledillusion.vaadin.cotton.demo;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("custom")
public class CustomComponentView extends Div {

    public CustomComponentView() {
        add(new AwesomeFieldBuilder().setExactWidth(250).setAwesomeText("<<replace with awesome>>").build());
    }
}
