# Chapter 3.a: Component Factorizing

Building UI in Vaadin requires creating and configuring lots and lots of UI components. Cotton contains a set of fluent component builders to cope with the repeated setter calling of components.

## The ComponentBuilders

In Cotton there is no such thing as _the_ **_ComponentBuilder_**; there is just a generic interface with that name.

When in need of building a specific **_Component_** implementation, the respective builder implementation has to be used:
- **_TextFieldBuilder_** for the **_TextField_** **_Component_**
- **_ComboBoxBuilder_** for the **_ComboBox_** **_Component_**
- ...

## Using a ComponentBuilder

Using Cotton's concrete component builders is super easy: create one and configure away. Once you are done, call build() to create a new instance of the builder's component type and apply your configuration:

````java
@Route("demo")
public class DemoView extends Div {

    public DemoView() {
        LabelBuilder labelBuilder = new LabelBuilder()
                .setExactWidth(100)
                .setHeightUndefined();

        add(labelBuilder.setValue("A").build(), labelBuilder.setValue("B").build());
    }
}
````

Noticed that we used the same builder twice? 

The builder contains its own configuration state, not in the component that is being build, but in itself. That way, if we have to build several similar components, we can reuse the builder and just change what has to be different in between calling the _build()_ method.

## Implementing own ComponentBuilders

The concrete **_ComponentBuilder_** implementations are specific to their type of **_Component_** and benefit of the way the functionality of **_Vaadin_**'s components is implemented: by implementing the _Has*_ interfaces. 

**_Vaadin_** contains a rather big set of interfaces whose name begins with _Has*_; those are implemented by components to standardize equal functionality. These interfaces describe a specific type of functionality, for example **_HasStyle_** for stylable components or **_HasValue_** for components that contain a value.

**_Cotton_**'s **_ComponentBuilder_** implementations use that functional abstraction by providing a set of _Has*Builder_ interfaces, which contain the building functionality for their component type via default methods. For example, the **_TextFieldBuilder_** implementation does not need to implement its own building functionality for the methods of **_HasSize_** that a **_TextField_** offers; the **_TextFieldBuilder_** simply implements **_HasSizeBuilder_** and is all set.

As a result, the concrete builders for a specific component type only have to contain such functionality that is not declared by a _Has*_ interface and remain rather slim.

When you implement a **_ComponentBuilder_** for a third party type of **_Component_**, simply implement the _Has*Builder_ interfaces and 90% of the work is done; now you can concentrate on the special functionality your specific **_Component_** provides.

So let's first create a custom component:

````java
public class AwesomeField extends Composite<TextField> implements HasSize {

    public AwesomeField() {
        getContent().setLabel("Write something awesome!");
    }

    public void setAwesomeText(String text) {
        getContent().setValue(text);
    }
}
````

It implements the **_HasSize_** interface and provides one own specific method; both should be configurable with our builder:

````java
public class AwesomeFieldBuilder extends AbstractComponentBuilder<AwesomeField, AwesomeFieldBuilder>
		implements HasSizeBuilder<AwesomeField, AwesomeFieldBuilder> {

    @Override
    public AwesomeField instantiate() {
        return new AwesomeField();
    }

    public AwesomeFieldBuilder setAwesomeText(String text) {
        configure(field -> field.setAwesomeText(text));
        return this;
    }
}
````

We have implemented the **_HasSizeBuilder_**, so we also have its whole functionality. Also, we have created a method to configure our special functionality by creating an instance of the functional interface **_Configurer_**. When _build()_ is called on our builder, that **_Configurer_** will be applied on a new instance of our custom component.

That's all! We can now use our builder:

````java
@Route("custom")
public class CustomComponentView extends Div {

    public CustomComponentView() {
        add(new AwesomeFieldBuilder().setExactWidth(250).setAwesomeText("<<replace with awesome>>").build());
    }
}
````