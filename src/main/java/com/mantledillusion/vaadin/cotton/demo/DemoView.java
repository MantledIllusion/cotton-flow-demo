package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.data.epiphy.ModelProperty;
import com.mantledillusion.injection.hura.core.annotation.injection.Inject;
import com.mantledillusion.vaadin.cotton.component.builders.ButtonBuilder;
import com.mantledillusion.vaadin.cotton.component.builders.TextFieldBuilder;
import com.mantledillusion.vaadin.cotton.demo.model.Model;
import com.mantledillusion.vaadin.cotton.demo.model.SubObject;
import com.mantledillusion.vaadin.cotton.model.ModelAccessor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

@Route("demo")
public class DemoView extends Div {

    private static final ModelProperty<Model, String> PROPERTY = ModelProperty.fromObject(Model::getSubObject).
            append(ModelProperty.fromObject(SubObject::getField));

    public DemoView(@Inject ModelAccessor<Model> accessor) {
        add(new TextFieldBuilder().setLabel("Field").bind(accessor, PROPERTY));

        add(new ButtonBuilder().setText("Exchange Model").addClickListener(event -> {
            Model model = new Model();
            model.setSubObject(new SubObject());
            model.getSubObject().setField(String.valueOf((int) (Math.random() * 10)));
            accessor.setModel(model);
        }).build());
    }
}