package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.injection.hura.core.annotation.instruction.Define;
import com.mantledillusion.injection.hura.web.HuraWebApplicationInitializer;
import com.mantledillusion.injection.hura.web.env.WebEnvironmentFactory;
import com.mantledillusion.vaadin.cotton.CottonEnvironment;
import com.mantledillusion.vaadin.cotton.CottonServlet;

import java.util.Optional;

public class DemoApplicationInitializer implements HuraWebApplicationInitializer {

    public static final String QUALIFIER_APPLVL_BEAN = "appLvlBeanQualifier";

    @Define
    public SingletonAllocation defineCottonServlet() {
        return WebEnvironmentFactory
                .registerServlet(CottonServlet.class)
                .addMapping("/*")
                .build();
    }

    @Define
    public PropertyAllocation defineActivateAutomaticRouteDiscovery() {
        return CottonEnvironment.forAutomaticRouteDiscovery(true);
    }

    @Define SingletonAllocation defineAppLevelSingleton() {
        return SingletonAllocation.of(QUALIFIER_APPLVL_BEAN, Optional.of("someString"));
    }
}