package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.vaadin.cotton.WebEnv;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("login")
public class DemoLoginView extends VerticalLayout {

    public DemoLoginView() {
        setSizeFull();
        CheckboxGroup<String> rightGroup = new CheckboxGroup<>();
        rightGroup.setItems("right_a", "right_b", "right_c");
        add(rightGroup);

        Button b = new Button("Login");
        b.addClickListener(event -> WebEnv.logIn(requiredRights -> rightGroup.getSelectedItems().containsAll(requiredRights)));
        add(b);
    }
}
