package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.injection.hura.core.annotation.injection.Inject;
import com.mantledillusion.vaadin.cotton.component.builders.HorizontalLayoutBuilder;
import com.mantledillusion.vaadin.cotton.viewpresenter.Responsive;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.Route;

@Route("demo")
@Responsive(@Responsive.Alternative(value = PortraitView.class, mode = Responsive.Alternative.ScreenMode.RATIO,
        fromX = 1, fromY = 1, toX = 1, toY = Integer.MAX_VALUE))
public class LandscapeView extends AbstractResponsiveView {

    @Inject
    private ResponsiveDialogView responsiveDialogView;

    @Override
    Component wrapInLayout(Component[] components) {
        return HorizontalLayoutBuilder.create().setWidthUndefined().setHeightFull().add(components).build();
    }
}