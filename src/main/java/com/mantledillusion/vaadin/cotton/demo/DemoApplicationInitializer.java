package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.injection.hura.core.annotation.instruction.Define;
import com.mantledillusion.injection.hura.web.HuraWebApplicationInitializer;
import com.mantledillusion.injection.hura.web.env.WebEnvironmentFactory;
import com.mantledillusion.vaadin.cotton.CottonServlet;

public class DemoApplicationInitializer implements HuraWebApplicationInitializer {

    @Define
    public SingletonAllocation defineCottonServlet() {
        return WebEnvironmentFactory
                .registerServlet(CottonServlet.class)
                .addMapping("/*")
                .build();
    }
}