package com.mantledillusion.vaadin.cotton.demo;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("failure")
public class FailureView extends Div {

    public FailureView() {
        throw new RuntimeException("Error!");
    }
}