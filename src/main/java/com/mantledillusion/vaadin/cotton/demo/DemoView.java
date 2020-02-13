package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.data.epiphy.ModelProperty;
import com.mantledillusion.injection.hura.core.annotation.injection.Inject;
import com.mantledillusion.vaadin.cotton.User;
import com.mantledillusion.vaadin.cotton.WebEnv;
import com.mantledillusion.vaadin.cotton.component.builders.ButtonBuilder;
import com.mantledillusion.vaadin.cotton.component.builders.HorizontalLayoutBuilder;
import com.mantledillusion.vaadin.cotton.component.builders.TextFieldBuilder;
import com.mantledillusion.vaadin.cotton.model.Binding;
import com.mantledillusion.vaadin.cotton.model.ModelAccessor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("demo")
public class DemoView extends VerticalLayout {

    private static final ModelProperty<Model, String> SENSITIVE_PROPERTY =
            ModelProperty.fromObject(Model::getSensitiveField, Model::setSensitiveField);
    private static final ModelProperty<Model, String> SUPER_SENSITIVE_PROPERTY =
            ModelProperty.fromObject(Model::getSuperSensitiveField, Model::setSuperSensitiveField);

    public DemoView(@Inject ModelAccessor<Model> accessor) {
        Model model = new Model();
        model.setSensitiveField("Some sensitive value");
        model.setSuperSensitiveField("Some super sensitive value");
        accessor.setModel(model);

        add(new HorizontalLayoutBuilder().
                add(new ButtonBuilder().
                        setValue("Login Power User").
                        addClickListener(e -> WebEnv.logIn(DemoUser.USER_POWER)).
                        build()).
                add(new ButtonBuilder().
                        setValue("Login Casual User").
                        addClickListener(e -> WebEnv.logIn(DemoUser.USER_CASUAL)).
                        build()).
                add(new ButtonBuilder().
                        setValue("Login Guest User").
                        addClickListener(e -> WebEnv.logIn(DemoUser.USER_GUEST)).
                        build()).
                add(new ButtonBuilder().
                        setValue("Logout").
                        addClickListener(e -> WebEnv.logOut()).
                        build()).
                build());

        accessor.withBaseRestriction(User.UserRightBindingAuditor.forUser(Binding.AccessMode.MASKED));

        add(new HorizontalLayoutBuilder().
                add(new TextFieldBuilder().
                        setLabel("Sensitive Field").
                        bindAndConfigure(accessor, SENSITIVE_PROPERTY).
                        withRestriction(User.UserRightBindingAuditor.forUser(Binding.AccessMode.READ_WRITE, DemoUser.RIGHT_SUPER_SENSITIVE_DATA)).
                        withRestriction(User.UserRightBindingAuditor.forUser(Binding.AccessMode.READ_WRITE, DemoUser.RIGHT_SENSITIVE_DATA)).
                        bind()).
                add(new TextFieldBuilder().
                        setLabel("Super Sensitive Field").
                        bindAndConfigure(accessor, SUPER_SENSITIVE_PROPERTY).
                        withRestriction(User.UserRightBindingAuditor.forUser(Binding.AccessMode.READ_WRITE, DemoUser.RIGHT_SUPER_SENSITIVE_DATA)).
                        withRestriction(User.UserRightBindingAuditor.forUser(Binding.AccessMode.READ_ONLY, DemoUser.RIGHT_SENSITIVE_DATA)).
                        bind()).
                build());
    }
}