package com.mantledillusion.vaadin.cotton.demo;

import com.mantledillusion.data.epiphy.ModelProperty;
import com.mantledillusion.data.epiphy.ModelPropertyList;
import com.mantledillusion.injection.hura.core.annotation.injection.Inject;
import com.mantledillusion.vaadin.cotton.component.builders.GridBuilder;
import com.mantledillusion.vaadin.cotton.component.builders.TextFieldBuilder;
import com.mantledillusion.vaadin.cotton.component.mixin.HasDataProviderBuilder;
import com.mantledillusion.vaadin.cotton.model.ModelAccessor;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Route("demo")
public class DemoView extends VerticalLayout {

    private static final ModelPropertyList<List<Model>, Model> ELEMENT_LIST = ModelPropertyList.from();
    private static final ModelProperty<List<Model>, Model> ELEMENT = ELEMENT_LIST.append(ModelProperty.fromList());

    private static final class Filter extends HasDataProviderBuilder.BasicConfigurableFilter<Model> {

        private String fieldFilter = "";

        void setFieldFilter(String fieldFilter) {
            this.fieldFilter = fieldFilter;
            notifyConfigurationChanged();
        }

        @Override
        public boolean test(Model model) {
            return model.getField().startsWith(this.fieldFilter);
        }
    }

    public DemoView(@Inject ModelAccessor<List<Model>> accessor) {
        setWidthFull();
        setHeightFull();

        Grid<Model> grid = GridBuilder.create(Model.class, Filter.class).
                setSizeFull().
                setDataProvider(accessor, ELEMENT, new Filter()).
                configureColumn(Model::getField).
                setFilter(TextFieldBuilder.create().setLabel("Field"), Filter::setFieldFilter).
                add().
                build();
        add(grid);

        accessor.setModel(IntStream.range(0, 1000).mapToObj(String::valueOf).map(Model::new).collect(Collectors.toList()));
    }
}