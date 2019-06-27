# Chapter 3.c: Presenting Views

The MVP (Model/View/Presenter) pattern is a popular architectural pattern that can be applied to basically all types of frontends.

Cotton supports MVP by providing functionality of creating and hooking a presenter to a view, without allowing the view to be able to obtain a reference to that presenter.

# The Basics

In the MVP pattern, the **V**iew exposes data of some kind, while the **P**resenter controls how that data is exposed in relation to the circumstances and actions occurring. The idea is that this way, the code for business logic is cleanly separated from the one that is just required for displaying data, setting the path for a more controlled application growth.

On the view side, the interface **_Presentable_** has to be implemented. It contains just 1 method to register active components, which is _noop_ default-implemented in case registering active components is not needed. Since most self implemented views require being a **_Component_** (but hide which one) and also require being presented, **_Cotton_** offers the **_AbstractView_** class that both extends **_Vaadin_**'s **_Composite_** and implements **_Cotton_**'s **_Presentable_**.

On the presenter side, the interface **_Presenter_** has to be implemented. It also contains 1 noop default-implemented method, which is a setter of the view the presenter instance presents. Most presenters require having access to the view they present, so **_Cotton_** offers the class **_AbstractPresenter_** that simply implements **_Presenter_** and provides access to its view via getter.

To connect view and presenter, the **_Presentable_** implementation has to be annotated with _@Presented_, defining the **_Presenter_** to use. When a view is injected, Cotton will automatically also inject an instance of the presenter and link it to the view instance.

# Registering active Components

Let's put that functionality to the test, staring with the **_DemoView_** becoming a **_Presentable_** (and since we need it to be a **_Component_** so it can be _@Route_ annotated, we extend **_AbstractView_**):

````java
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
````

So we just declared that our view consists of a **_TextField_** with the id "input"; a component id is required so we can register the field on the active component registry.

Active components are components that are marked for firing events a **_Presenter_** might be interested in.

We also declared a getter and setter, so the presenter will not need direct access to the view's field.

# Listening to active Components

To control the **_DemoView_**, we create a **_DemoPresenter_**; instead of simply implementing **_Presenter_**, we extend **_AbstractPresenter_**, so we have direct access to the view instance:

````java
public class DemoPresenter extends AbstractPresenter<DemoView> {

    @Listen("input")
    private void handleEnter(KeyPressEvent event) {
        if (Key.ENTER.getKeys().stream().anyMatch(event.getKey()::matches)) {
            getView().setValue(getView().getValue()+'#');
        }
    }
}
````

Note we annotated the method with _@Listen_ and provided the component id of the component we want to listen to. Since we specified the event type in as the method parameter, the method will only be called when that specific event type occurs.