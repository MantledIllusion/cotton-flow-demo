# Chapter 4.a: Deep Model Binding

The Epiphy framework enables access to properties that are deep down a model's object tree only while only working with the tree's root. Cotton uses this feature to bind Vaadin components implementing the _**HasValue**_ interface to data deep down in an object tree.

## The ModelProperty

In Epiphy a value to an object is called property. 

A property in Epiphy describes the relation between the object and its value. For standard single values, that relation is instantiated by the class **_ModelProperty_**, while there are also variants for non-single values like **_ModelPropertyList_**.

Since it describes the relation between an object and a value (as a type of parent/child relationshiop), a **_ModelProperty_** always carries the types of those two factors as generic parameters. So for the imaginary parent type **A** and child type **B**, the property will be described as _**ModelProperty**<A, B>_ and offer operations that can be executed on an instance of **A** in order to access its instance of **B**.

To allow access to deep values, properties in Epiphy can be appended to each other as long as the child type of the prepended property equals the parent type of the appended one. In relation to the example, the _**ModelProperty**<A,B>_ can be appended with a _**ModelProperty**<B, C>_, with the result being a _**ModelProperty**<A, C>_ that allows direct access to the type **C** value on a type **A** model without having to deal with the type **B** instance in between. 

## Setting up a Model

To bind a deep model property, we first set up a very simple model with to layers of POJOs and a **_String_** value:

```java
public class Model {

    private SubObject subObject;

    public SubObject getSubObject() {
        return subObject;
    }

    public void setSubObject(SubObject subObject) {
        this.subObject = subObject;
    }
}
```

```java
public class SubObject {

    private String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
```

## Creating a **_ModelProperty_**

All **_ModelProperty_** classes from Epiphy have static factory methods to create properties with; also they provide builder methods to link multiple properties together when creating deep model properties.

As a result, we can create a **_ModelProperty_** for the two layer model's field in one go:

```java
@Route("demo")
public class DemoView extends Div {

    private static final ModelProperty<Model, String> PROPERTY = ModelProperty.fromObject(Model::getSubObject).
            append(ModelProperty.fromObject(SubObject::getField));

    ...
}
```

This property will accept instances of the **_Model_** class, but allow working with the **_String_** field of the **_SubObject_** class.

## Binding on a property

In Cotton, the **_ModelAccessor_** class allows binding properties to an object instance of those properties' parent type.

Instances of the class have to be injected to ensure their life cycle is taking care of:

```java
@Route("demo")
public class DemoView extends Div {

    private static final ModelProperty<Model, String> PROPERTY = ModelProperty.fromObject(Model::getSubObject).
            append(ModelProperty.fromObject(SubObject::getField));

    public DemoView(@Inject ModelAccessor<Model> accessor) {
        add(new TextFieldBuilder().setLabel("Field").bind(accessor, PROPERTY));

        ...
    }
}
```

All component builders building **_HasValue_** components allow having a **_ModelAccessor_** and a property being given for binding. Alternatively, the accessor also allows binding any HasValue to be bound directly by calling one of the _bind()_ methods.

Using the property, the accessor will bind the component to a model the accessor is carrying. As a result, any change to the value of the **_HasValue_** will be synced into the object model of the accessor. Also, exchanging that object of the **_ModelAccessor_** will cause the component's value to be refreshed:

```java
@Route("demo")
public class DemoView extends Div {

    private static final ModelProperty<Model, String> PROPERTY = ModelProperty.fromObject(Model::getSubObject).
            append(ModelProperty.fromObject(SubObject::getField));

    public DemoView(@Inject ModelAccessor<Model> accessor) {
        add(new TextFieldBuilder().setLabel("Field").bind(accessor, PROPERTY));

        add(new ButtonBuilder().setText("Exchange Model").addClickListener(event -> {
            Model model = new Model();
            model.setSubObject(new SubObject());
            model.getSubObject().setField(String.valueOf((int) (Math.random() * 10)));
            accessor.setModel(model);
        }).build());
    }
}
```