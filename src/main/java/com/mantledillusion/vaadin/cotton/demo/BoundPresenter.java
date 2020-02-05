package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.injection.hura.core.annotation.injection.Inject;
import com.mantledillusion.injection.hura.core.annotation.injection.Qualifier;
import com.mantledillusion.injection.hura.core.annotation.lifecycle.bean.PostConstruct;
import com.mantledillusion.vaadin.cotton.model.ModelContainer;
import com.mantledillusion.vaadin.cotton.viewpresenter.AbstractPresenter;

public class BoundPresenter extends AbstractPresenter<BoundView> {

    @Inject
    @Qualifier(ModelContainer.SID_CONTAINER)
    private ModelContainer<Model> container;

    @PostConstruct
    private void setModel() {
        Model model = new Model();
        model.setField("Start");
        this.container.setModel(model);
    }
}
