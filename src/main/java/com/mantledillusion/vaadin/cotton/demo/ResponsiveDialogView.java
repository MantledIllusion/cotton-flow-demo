package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.vaadin.cotton.component.builders.ButtonBuilder;
import com.mantledillusion.vaadin.cotton.component.builders.HorizontalLayoutBuilder;
import com.mantledillusion.vaadin.cotton.component.builders.LabelBuilder;
import com.mantledillusion.vaadin.cotton.component.builders.VerticalLayoutBuilder;
import com.mantledillusion.vaadin.cotton.viewpresenter.AbstractView;
import com.mantledillusion.vaadin.cotton.viewpresenter.Presented;
import com.vaadin.flow.component.Component;

@Presented(ResponsiveDialogPresenter.class)
public class ResponsiveDialogView extends AbstractView {

    static final String CID_YES = "yes";
    static final String CID_NO = "no";

    @Override
    protected Component buildUI(TemporalActiveComponentRegistry reg) throws Exception {
        return VerticalLayoutBuilder.create().
                add(LabelBuilder.create().setValue("Do you want to switch?").build()).
                add(HorizontalLayoutBuilder.create().
                        add(ButtonBuilder.create().setText("Yes").setRegistration(reg, CID_YES).build()).
                        add(ButtonBuilder.create().setText("No").setRegistration(reg, CID_NO).build()).
                        build()).
                build();
    }
}
