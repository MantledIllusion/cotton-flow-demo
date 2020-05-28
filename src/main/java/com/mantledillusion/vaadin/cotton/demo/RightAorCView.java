package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.vaadin.cotton.WebEnv;
import com.mantledillusion.vaadin.cotton.viewpresenter.PrioritizedRouteAlias;
import com.mantledillusion.vaadin.cotton.viewpresenter.Restricted;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("demo/rightAorC")
@PrioritizedRouteAlias(value = "demo", priority = 2)
@Restricted("right_a || right_c")
public class RightAorCView extends Div {

    public RightAorCView() {
        Button b = new Button("Logout");
        b.addClickListener(event -> WebEnv.logOut());
        add(new VerticalLayout(new Label("Accessed with right A or C!"), b));
    }
}