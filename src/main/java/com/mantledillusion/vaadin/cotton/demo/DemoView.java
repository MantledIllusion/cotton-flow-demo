package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.metrics.trail.VaadinMetricsTrailSupport;
import com.mantledillusion.metrics.trail.api.Metric;
import com.mantledillusion.metrics.trail.api.MetricType;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

import java.util.function.Consumer;

@Route("demo")
public class DemoView extends Div {

    private static final Consumer<ClickEvent> METRICS_DISPATCHER = event ->
        VaadinMetricsTrailSupport.getCurrent().commit(new Metric("clickmetric", MetricType.ALERT));

    public DemoView() {
        Button b = new Button("Create Click Metric");
        b.addClickListener(METRICS_DISPATCHER::accept);
        add(b);
    }
}