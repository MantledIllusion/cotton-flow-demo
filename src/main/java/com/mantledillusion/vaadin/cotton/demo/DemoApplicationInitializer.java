package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.injection.hura.core.annotation.instruction.Define;
import com.mantledillusion.injection.hura.web.HuraWebApplicationInitializer;
import com.mantledillusion.injection.hura.web.env.WebEnvironmentFactory;
import com.mantledillusion.vaadin.cotton.CottonEnvironment;
import com.mantledillusion.vaadin.cotton.CottonServlet;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

public class DemoApplicationInitializer implements HuraWebApplicationInitializer {

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

    @Define
    public List<SingletonAllocation> defineTechnicalLocalizations() {
        return CottonEnvironment.forLocalization("technical", "i18n",
                Charset.forName("UTF-8"), Locale.ENGLISH, Locale.FRENCH);
    }

    @Define
    public List<SingletonAllocation> defineTranslationLocalizations() {
        return CottonEnvironment.forLocalization("translations", "i18n",
                Charset.forName("UTF-8"), Locale.ENGLISH, Locale.FRENCH);
    }
}