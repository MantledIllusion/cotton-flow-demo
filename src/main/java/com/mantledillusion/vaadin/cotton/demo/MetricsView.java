package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.injection.hura.core.annotation.injection.Inject;
import com.mantledillusion.injection.hura.core.annotation.injection.Qualifier;
import com.mantledillusion.metrics.trail.api.Metric;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

@Route("metrics")
public class MetricsView extends Div {

    private static final Function<Metric, String> OPERATOR_RETRIEVER = metric ->
            StringUtils.join(metric.getAttributes(), ", ");

    public MetricsView(@Inject @Qualifier(DemoMetricsCache.QUALIFIER_CACHE) DemoMetricsCache cache) {
        Grid<Metric> grid = new Grid<>();
        grid.addColumn(Metric::getIdentifier).setHeader("Identifier").setFlexGrow(0).setWidth("200px");
        grid.addColumn(Metric::getType).setHeader("Type").setFlexGrow(0);
        grid.addColumn(Metric::getTimestamp).setHeader("Timestamp").setFlexGrow(0).setWidth("300px");
        grid.addColumn(OPERATOR_RETRIEVER::apply).setHeader("Operator").setFlexGrow(1);
        add(grid);

        grid.setItems(cache.getSessionMetrics());
    }
}