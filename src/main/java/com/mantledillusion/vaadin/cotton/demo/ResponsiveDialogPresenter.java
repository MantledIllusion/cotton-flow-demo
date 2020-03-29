package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.injection.hura.core.annotation.lifecycle.bean.PostConstruct;
import com.mantledillusion.injection.hura.core.annotation.lifecycle.bean.PostDestroy;
import com.mantledillusion.vaadin.cotton.CottonUI;
import com.mantledillusion.vaadin.cotton.WebEnv;
import com.mantledillusion.vaadin.cotton.event.responsive.BeforeResponsiveRefreshEvent;
import com.mantledillusion.vaadin.cotton.viewpresenter.AbstractPresenter;
import com.mantledillusion.vaadin.cotton.viewpresenter.Listen;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.shared.Registration;

public class ResponsiveDialogPresenter extends AbstractPresenter<ResponsiveDialogView> implements CottonUI.BeforeResponsiveRefreshListener {

    private Dialog dialog;
    private Registration registration;

    @PostConstruct
    private void init() {
        this.registration = CottonUI.current().addBeforeResponsiveRefreshListener(this);
        this.dialog = new Dialog(getView());
        this.dialog.close();
    }

    @Override
    public void beforeRefresh(BeforeResponsiveRefreshEvent event) {
        if (!event.isForced()) {
            event.decline();
            if (!this.dialog.isOpened()) {
                this.dialog.open();
            }
        }
    }

    @Listen(ResponsiveDialogView.CID_YES)
    private void handleYes(ClickEvent e) {
        this.dialog.close();
        WebEnv.adaptResponsive();
    }

    @Listen(ResponsiveDialogView.CID_NO)
    private void handleNo(ClickEvent e) {
        this.dialog.close();
    }

    @PostDestroy
    private void destroy() {
        this.registration.remove();
    }
}
