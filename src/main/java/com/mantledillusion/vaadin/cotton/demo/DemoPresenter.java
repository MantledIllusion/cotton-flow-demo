package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.vaadin.cotton.viewpresenter.AbstractPresenter;
import com.mantledillusion.vaadin.cotton.viewpresenter.Listen;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyPressEvent;

public class DemoPresenter extends AbstractPresenter<DemoView> {

    @Listen("input")
    private void handleEnter(KeyPressEvent event) {
        if (Key.ENTER.getKeys().stream().anyMatch(event.getKey()::matches)) {
            getView().setValue(getView().getValue()+'#');
        }
    }
}