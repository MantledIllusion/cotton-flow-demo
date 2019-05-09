package com.mantledillusion.vaadin.cotton.demo;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("demo")
public class DemoView extends VerticalLayout {

    public DemoView() {
        setSizeFull();

        HorizontalLayout columnLayout = new HorizontalLayout();
        columnLayout.setHeightFull();
        add(columnLayout);
        setHorizontalComponentAlignment(Alignment.CENTER, columnLayout);

        Label helloLabel = new Label("Hello from your first Cotton app!");
        columnLayout.add(helloLabel);
        columnLayout.setVerticalComponentAlignment(Alignment.CENTER, helloLabel);
    }
}
