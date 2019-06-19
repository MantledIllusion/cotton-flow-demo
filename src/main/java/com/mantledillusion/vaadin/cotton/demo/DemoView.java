package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.vaadin.cotton.WebEnv;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

@Route("demo")
public class DemoView extends Div {

    public DemoView() {
        add(new HorizontalLayout(
                new Label(getTranslation("identifier.a")),
                new Label(WebEnv.getTranslation("identifier.b"))));
    }
}