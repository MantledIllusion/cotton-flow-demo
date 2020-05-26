package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.vaadin.cotton.User;
import com.mantledillusion.vaadin.cotton.WebEnv;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
@Route("login")
public class DemoLoginView extends VerticalLayout {

    private static final User USER = rightIds -> Arrays.asList("right_a", "right_c").containsAll(rightIds);

    public DemoLoginView() {
        setSizeFull();

        Button b = new Button("Login");
        b.addClickListener(event -> WebEnv.logIn(USER));
        add(b);
    }
}
