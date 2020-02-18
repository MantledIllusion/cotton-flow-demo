package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.data.epiphy.ModelProperty;
import com.mantledillusion.data.epiphy.ModelPropertyList;
import com.mantledillusion.injection.hura.core.annotation.injection.Inject;
import com.mantledillusion.vaadin.cotton.component.builders.ButtonBuilder;
import com.mantledillusion.vaadin.cotton.component.builders.GridBuilder;
import com.mantledillusion.vaadin.cotton.model.ModelAccessor;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route("demo")
public class DemoView extends VerticalLayout {

    private static final ModelPropertyList<List<Model>, Model> ELEMENT_LIST = ModelPropertyList.from();
    private static final ModelProperty<List<Model>, Model> ELEMENT = ELEMENT_LIST.append(ModelProperty.fromList());

    public DemoView(@Inject ModelAccessor<List<Model>> accessor) {
        setWidthFull();
        setHeightFull();

        Grid<Model> grid = new GridBuilder<Model>().
                setSizeFull().
                provide(accessor, ELEMENT).
                build();
        grid.addColumn(Model::getField);
        add(grid);

        add(new ButtonBuilder().
                setText("Add X Element").
                addClickListener(e -> accessor.include(ELEMENT_LIST, new Model("X"))).
                build());
        add(new ButtonBuilder().
                setText("Remove Last Element").
                addClickListener(e -> accessor.strip(ELEMENT_LIST)).
                build());

        accessor.setModel(new ArrayList<>(Arrays.asList(new Model("A"), new Model("B"), new Model("C"))));
    }
}