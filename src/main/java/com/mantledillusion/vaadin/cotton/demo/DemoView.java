package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.injection.hura.core.annotation.injection.Inject;
import com.mantledillusion.injection.hura.core.annotation.injection.Qualifier;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.Route;

import java.util.Optional;

@Route("demo")
public class DemoView extends Div {

    public DemoView(@Inject @Qualifier(DemoApplicationInitializer.QUALIFIER_APPLVL_BEAN) Optional<String> appLvlBean) {
        add(new Label(appLvlBean.get()));
    }
}