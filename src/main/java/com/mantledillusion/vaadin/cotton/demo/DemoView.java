package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.metrics.trail.MetricsTrailSupport;
import com.mantledillusion.metrics.trail.api.Metric;
import com.mantledillusion.metrics.trail.api.MetricType;
import com.mantledillusion.vaadin.cotton.WebEnv;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

import java.util.function.Consumer;

@Route("demo")
public class DemoView extends HorizontalLayout {

    private static final Consumer<ClickEvent> METRICS_DISPATCHER = event ->
            MetricsTrailSupport.commit(new Metric("clickmetric", MetricType.ALERT));

    public DemoView() {
        Button b = new Button("Create Click Metric");
        b.addClickListener(METRICS_DISPATCHER::accept);
        add(b);

        b = new Button("Cause Error");
        b.addClickListener(event -> {
            throw new RuntimeException("Boo-Hoo!");
        });
        add(b);

        b = new Button("Nav Error");
        b.addClickListener(event -> WebEnv.navigateTo(FailureView.class));
        add(b);
    }
}