package com.mantledillusion.vaadin.cotton.demo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractResponsiveView extends Div {

    public AbstractResponsiveView() {
        add(wrapInLayout(IntStream.range(0, 100).
                mapToObj(i -> new Label("This is the #"+i+" content")).
                collect(Collectors.toList()).
                toArray(new Component[0])));
    }

    abstract Component wrapInLayout(Component[] components);
}
