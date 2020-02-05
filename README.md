# Chapter 4.b MVP-Compliant Data Binding

Cotton's MVP functionality allows applications to be split into view and logic code cleanly; and that goes for the data binding as well, where the model should be controlled elsewhere than it is bound.

## Setting up a Model

For this lesson we do not need a complex model, a simple one with one **_String_** field is enough:

```java
public class Model {
    
    private String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
```

## Setting up the Views

We are using Cotton, so if we want to have MVP compliant views, we use the **_AbstractView_** super class for our views.

Then we inject a **_ModelAccessor_** to retrieve our model from and bind a **_TextField_** to our **_String_** field. For the binding we create a **_ModelProperty_** that uses both getter <u>and</u> setter of our model's field, so the binding is able to write changed values in the **_TextField_** back into the model.

```java
@Presented(BoundPresenter.class)
public class BoundView extends AbstractView {

    private static final ModelProperty<Model, String> FIELD = ModelProperty.fromObject(Model::getField, Model::setField);

    @Inject
    private ModelAccessor<Model> accessor;

    @Override
    protected Component buildUI(TemporalActiveComponentRegistry reg) throws Exception {
        return new TextFieldBuilder().bind(this.accessor, FIELD);
    }
}
```

Also we have annotated our view with _@Presented_ which points to the presenter we want to control the model by.

We want to have multiple bindings, so we change our **_DemoView_** to inject two instances of **_BoundView_**:
```java
@Route("demo")
public class DemoView extends Div {

    public DemoView(@Inject BoundView a, @Inject BoundView b) {
        add(a, b);
    }
}
```

## Setting up the Presenter

For the purpose of this lesson it is enough to let the presenter set a model to start with (of course it could do much more if desired):

```java
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
```

The injected **_ModelContainer_** is the instance of a class that just wraps a model instance.

The important part here is about its _@Qualifier_ annotation; it causes it to be injected as a singleton, in this case with the default qualifier of containers. Since **_ModelAccessor_** instances all contain a **_ModelContainer_** to retrieve their data from and also use this qualifier by default, the injected container of our **_BoundPresenter_** will be the exact same instance than in all of the **_BoundView_**'s accessors.

As a result, when changes are done to the model by a binding of an accessor in a **_BoundView_**, those changes will be mirrored to the bindings of other accessor instances as well. For our demo setup with two instances of **_BoundView_** injected into **_DemoView_**, changing the value of one **_TextField_** will cause the value of the other **_TextField_** to change as well.

This is called bi-directional binding.