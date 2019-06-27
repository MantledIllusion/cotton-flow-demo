package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.vaadin.cotton.viewpresenter.AbstractView;
import com.mantledillusion.vaadin.cotton.viewpresenter.Presented;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("demo")
@Presented(DemoPresenter.class)
public class DemoView extends AbstractView {

    private TextField textField;

    @Override
    protected Component buildUI(TemporalActiveComponentRegistry reg) {
        this.textField = new TextField();
        this.textField.setId("input");
        reg.register(this.textField);
        return this.textField;
    }

    public String getValue() {
        return this.textField.getValue();
    }

    public void setValue(String value) {
        this.textField.setValue(value);
    }
}