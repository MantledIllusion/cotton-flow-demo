package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.metrics.trail.MetricsTrailSupport;
import com.mantledillusion.metrics.trail.api.Metric;
import com.mantledillusion.vaadin.cotton.metrics.CottonMetrics;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DemoMetricsCache {

    public static final String QUALIFIER_CACHE = "_demoMetricsCache";

    private final Map<UUID, List<Metric>> trailCache = new ConcurrentHashMap<>();

    public void add(UUID trailId, Metric metric) {
        if (CottonMetrics.SESSION_END.getMetricId().equals(metric.getIdentifier())) {
            this.trailCache.remove(trailId);
        } else {
            this.trailCache.computeIfAbsent(trailId, id -> Collections.synchronizedList(new ArrayList<>())).add(metric);
        }
    }

    public List<Metric> getSessionMetrics() {
        if (!MetricsTrailSupport.has() || !this.trailCache.containsKey(MetricsTrailSupport.id())) {
            return Collections.emptyList();
        } else {
            return this.trailCache.get(MetricsTrailSupport.id());
        }
    }
}
