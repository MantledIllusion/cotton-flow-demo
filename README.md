# Chapter 3.b: The EventBus

As frontends grow, there sooner or later will always be a need for sending messages between separated parts of the application. To prevent class dependency hell, it is better to decouple those parts since they do not need know each other, but make them exchange messages anonymously via event bus.

The injection framework Hura which is included in Cotton offers a build-in event bus, where subscribing beans to that bus are automatically hooked on and off with their life cycle. Cotton pre-configures that bus, so no events of session beans can leave their own session, making it impossible to mistakenly alert bus subscribers that are not meant to be contacted.

## Sending & Receiving Events

Technically, **_Hura_**'s event bus is available to every bean injected by the framework.

To demonstrate this, we create a DemoSubView class that both receives and publishes events to **_Hura_**'s event bus:

````java
public class DemoSubView extends Div {

    private TextField field;

    public DemoSubView(@Inject Bus bus) {
        this.field = new TextField();
        this.field.addKeyPressListener(Key.ENTER, bus::publish);

        add(this.field);
    }

    @Subscribe
    private void receive(KeyPressEvent event) {
        this.field.setValue(((TextField) event.getSource()).getValue());
    }
}
````

The view requires an instance of the bus to be injected as constructor parameter; on every ENTER-key press in the view's textfield, the occurring **_KeyPressEvent_** is directly published to the bus.

Since the view itself is also a bean, it can also subscribe to events from the bus: so the receive() method is annotated with **_@Subscribe_** and receives the **_KeyPressEvent_** as parameter.

Now we inject 2 of those views into our **_DemoView_**:

````java
@Route("demo")
public class DemoView extends Div {

    public DemoView(@Inject DemoSubView a, @Inject DemoSubView b) {
        add(a, b);
    }
}
````

As we see, the 2 instances of the **_DemoSubView_** do not know each other by reference. But when the ENTER key is pressed while typing into one of the text fields, the event will be published to the bus, from which both views will receive it and set the value from it as their value.