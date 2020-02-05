package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.data.epiphy.ModelProperty;
import com.mantledillusion.injection.hura.core.annotation.injection.Inject;
import com.mantledillusion.vaadin.cotton.component.builders.TextFieldBuilder;
import com.mantledillusion.vaadin.cotton.model.ModelAccessor;
import com.mantledillusion.vaadin.cotton.viewpresenter.AbstractView;
import com.mantledillusion.vaadin.cotton.viewpresenter.Presented;
import com.vaadin.flow.component.Component;

@Presented(BoundPresenter.class)
public class BoundView extends AbstractView {

    private static final ModelProperty<Model, String> FIELD = ModelProperty.fromObject(Model::getField, Model::setField);

    @Inject
    private ModelAccessor<Model> accessor;

    @Override
    protected Component buildUI(TemporalActiveComponentRegistry reg) throws Exception {
        return new TextFieldBuilder().bind(this.accessor, FIELD);
    }
}
