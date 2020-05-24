package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.injection.hura.core.annotation.injection.Inject;
import com.mantledillusion.injection.hura.core.annotation.injection.Qualifier;
import com.mantledillusion.injection.hura.core.annotation.instruction.Define;
import com.mantledillusion.injection.hura.web.HuraWebApplicationInitializer;
import com.mantledillusion.injection.hura.web.env.WebEnvironmentFactory;
import com.mantledillusion.metrics.trail.MetricsConsumer;
import com.mantledillusion.vaadin.cotton.CottonEnvironment;
import com.mantledillusion.vaadin.cotton.CottonServlet;

public class DemoApplicationInitializer implements HuraWebApplicationInitializer {

    @Inject
    private DemoMetricsCache cache;

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
    public SingletonAllocation defineMetricsConsumer() {
        MetricsConsumer consumer = (consumerId, trailId, metric) -> this.cache.add(trailId, metric);
        return CottonEnvironment.forMetricsConsumer("demoConsumer", consumer, null, null);
    }

    @Define
    public SingletonAllocation defineCache() {
        return SingletonAllocation.allocateToInstance(DemoMetricsCache.QUALIFIER_CACHE, this.cache);
    }
}