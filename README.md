# Chapter 4.e: Binding Filtered DataProviders

**Cotton**'s **_HasDataProviderBuilder_** allows configuring filterable **Vaadin** **_DataProvider_** instances on the fly for components like **_Grid_**, _CheckBoxGroup_ and **_RadioButtonGroup_**; and in case of **_Grid_** even enables binding filter inpout fields for columns with a simple one-liner.

## Setting up a Model

For this lesson we do not need a complex model, a simple one with one String field is enough:

```java
class Model {

    private final String field;

    Model(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
```

## Setting up the View

The first thing required is a property whose values can occur multiple times in a model. For this lesson we use simply a listed one, but note that this can be any other type as well, might it be a mapped or noded one:

```java
@Route("demo")
public class DemoView extends VerticalLayout {

    private static final ModelPropertyList<List<Model>, Model> ELEMENT_LIST = ModelPropertyList.from();
    private static final ModelProperty<List<Model>, Model> ELEMENT = ELEMENT_LIST.append(ModelProperty.fromList());

    ...
}
```

Since we are filtering, we need a filter to store the filtering settings in. We can do that by implementing **_ConfigurableFilter_**; for convenience, we extend BasicConfigurableFilter instead which provides us we the needed logic for the listeners for changes to our filter :

```java
@Route("demo")
public class DemoView extends VerticalLayout {

    ...
    
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

    ...
}
```

The two important things here:
- we call _notifyConfigurationChanged()_ in our setter, so all listeners to the filter become aware of a change to it
- since we are filtering in-memory here (when binding to a **_Property_**, an _InMemoryDataProvider_ is created), we can directly filter by simply testing element instances using an overridden _test()_ method

Next, we bind a _**Grid**_ to our property, supply an instance of our filter and provide a **_TextField_** for the column for a filter input::

```java
@Route("demo")
public class DemoView extends VerticalLayout {

    ...

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

        ...
    }
}
```

There are LOTS of ways to both create a **_DataProvider_** and a filtering field; we used the most simple method here by binding to in-memory data; but the builder also allows to lazily load data by supplying callbacks to a data source, which would be preferable for filtered data from a database.

The only thing left to do for our lessen though is providing elements for our **_Grid_**:

```java
@Route("demo")
public class DemoView extends VerticalLayout {

    ...

    public DemoView(@Inject ModelAccessor<List<Model>> accessor) {
        ...

        accessor.setModel(IntStream.range(0, 1000).mapToObj(String::valueOf).map(Model::new).collect(Collectors.toList()));
    }
}
```