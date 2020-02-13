package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.data.epiphy.ModelProperty;
import com.mantledillusion.injection.hura.core.annotation.injection.Inject;
import com.mantledillusion.vaadin.cotton.User;
import com.mantledillusion.vaadin.cotton.WebEnv;
import com.mantledillusion.vaadin.cotton.component.builders.ButtonBuilder;
import com.mantledillusion.vaadin.cotton.component.builders.HorizontalLayoutBuilder;
import com.mantledillusion.vaadin.cotton.component.builders.TextFieldBuilder;
import com.mantledillusion.vaadin.cotton.model.ModelAccessor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("demo")
public class DemoView extends VerticalLayout {

    private static final ModelProperty<Model, String> FIELD = ModelProperty.fromObject(Model::getField, Model::setField);

    public DemoView(@Inject ModelAccessor<Model> accessor) {
        add(new HorizontalLayoutBuilder().
                add(new ButtonBuilder().
                        setValue("Login Editor").
                        addClickListener(e -> WebEnv.logIn(DemoUser.USER_EDITOR)).
                        build()).
                add(new ButtonBuilder().
                        setValue("Login Guest").
                        addClickListener(e -> WebEnv.logIn(DemoUser.USER_NONE)).
                        build()).
                add(new ButtonBuilder().
                        setValue("Logout").
                        addClickListener(e -> WebEnv.logOut()).
                        build()).
                build());

        add(new TextFieldBuilder().
                bindAndConfigure(accessor, FIELD).
                withRestriction(User.UserRightBindingAuditor.readWrite(DemoUser.RIGHT_EDITOR)).
                withRestriction(User.UserRightBindingAuditor.readOnly()).
                bind());

        Model model = new Model();
        model.setField("Some sensitive value");
        accessor.setModel(model);
    }
}