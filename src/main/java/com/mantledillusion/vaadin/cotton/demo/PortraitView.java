package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.injection.hura.core.annotation.injection.Inject;
import com.mantledillusion.vaadin.cotton.component.builders.VerticalLayoutBuilder;
import com.vaadin.flow.component.Component;

public class PortraitView extends AbstractResponsiveView {

    @Inject
    private ResponsiveDialogView responsiveDialogView;

    @Override
    Component wrapInLayout(Component[] components) {
        return VerticalLayoutBuilder.create().setWidthFull().setHeightUndefined().add(components).build();
    }
}
