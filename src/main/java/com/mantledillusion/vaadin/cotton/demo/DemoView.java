package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.vaadin.cotton.WebEnv;
import com.mantledillusion.vaadin.cotton.viewpresenter.Restricted;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

@Route("demo")
@Restricted("(right_a || right_b) && right_c")
public class DemoView extends Div {

    public DemoView() {
        Button b = new Button("Logout");
        b.addClickListener(event -> WebEnv.logOut());
        add(new HorizontalLayout(new Label("Accessed!"), b));
    }
}