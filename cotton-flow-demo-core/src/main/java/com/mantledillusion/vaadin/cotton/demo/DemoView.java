package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.injection.hura.core.annotation.injection.Plugin;
import com.mantledillusion.vaadin.cotton.viewpresenter.AbstractView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("demo")
public class DemoView extends Div {

    private DemoView(@Plugin(directory = "./cotton-flow-demo-viewa/target", pluginId = "ViewA") Component a,
                     @Plugin(directory = "./cotton-flow-demo-viewb/target", pluginId = "ViewB") Component b) {
        add(a, b);
    }
}