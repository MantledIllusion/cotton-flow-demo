package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.injection.hura.core.annotation.injection.Inject;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("demo")
public class DemoView extends Div {

    public DemoView(@Inject BoundView a, @Inject BoundView b) {
        add(a, b);
    }
}