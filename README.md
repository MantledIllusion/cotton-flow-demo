# Chapter 4.d: Numerated Model Binding

Binding a single field to a single property is trivial; but Epiphy's also allows numerated properties in a model tree, such as listed or mapped ones.

Cotton allows binding properties to **Vaadin**'s **_DataProvider_**, which can be directly used in components such as **_Grid_** or **_RadioButtonGroup_**.

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

Next, we bind a **_Grid_**'s **_DataProvider_** to that property:

```java
@Route("demo")
public class DemoView extends VerticalLayout {

    ...

    public DemoView(@Inject ModelAccessor<List<Model>> accessor) {
        setWidthFull();
        setHeightFull();

        Grid<Model> grid = GridBuilder.create(Model.class).
                setSizeFull().
                setDataProvider(accessor, ELEMENT).
                build();
        grid.addColumn(Model::getField);
        add(grid);

        ...
    }
}
```

We have also created a column for our **_Grid_** that displays our **_Model_**'s field. 

Note how we did not use the **_ModelPropertyList_** for providing the data, but the **_ModelProperty_**, as that is the one that occurs in a listed fashion. We only need the **_ModelPropertyList_** so we are able to manipulate that list, so the binding automatically refreshes our **_Grid_**:

```java
@Route("demo")
public class DemoView extends VerticalLayout {

    ...

    public DemoView(@Inject ModelAccessor<List<Model>> accessor) {
        ...

        add(ButtonBuilder.create().
                setText("Add X Element").
                addClickListener(e -> accessor.include(ELEMENT_LIST, new Model("X"))).
                build());
        add(ButtonBuilder.create().
                setText("Remove Last Element").
                addClickListener(e -> accessor.strip(ELEMENT_LIST)).
                build());

        ...
    }
}
```

Now, the only thing left to do is initialize our **_ModelContainer_** (note that for this lesson, we did not inject it explicitly; just implicitly with the **_ModelAccessor_**):

```java
@Route("demo")
public class DemoView extends VerticalLayout {

    ...

    public DemoView(@Inject ModelAccessor<List<Model>> accessor) {
        ...

        accessor.setModel(new ArrayList<>(Arrays.asList(new Model("A"), new Model("B"), new Model("C"))));
    }
}
```

When using the buttons, the model list will be modified; but since its done using the **_ModelAccessor_**, the **_Grid_**'s binding will be refreshed, causing it to be updated.