# Chapter 3.d: Building a Plugin UI

In some cases it might be a great idea so take modularization of a frontend to the next level; by linking together the parts as separated plugins. 

One example of such a situation might be the frontend for a _Service Ortiented Architecture_ (SOA). With the application cleanly separated (for example into services for customer, order and product using _Domain Driven Design_), changes to those services could be deployed at any time without affecting the rest of the platform.

But with a monolith frontend, every API changing service deployment would have the effect of the complete frontend having to be redeployed as well. Using a plugin based frontend (with the single plugins of the frontend shaped just as the service), it would be possible to keep the frontend up and running while just deploying an updated plugin for the service.

Since Cotton uses Hura 2, basic plugin functionality is already on board, ready to be used.

## Setting up a structure

Since we will build completely separated parts for the application we will need a multi-module Maven project:
- parent
    - api (contains common objects)
    - core (the frontend application)
    - viewa (the first plugin)
    - viewb (the second plugin)


## Creating an API

Of course, it would be possible to compose a frontend from plugins that share no common ground; but that would limit those plugins abilities to work together to zero.

For this example, we will just add a single class into the API:
````java
public class ApiEvent {

    private final Object source;
    private final String text;

    public ApiEvent(Object source,String text) {
        this.source = source;
        this.text = text;
    }

    public Object getSource() {
        return source;
    }

    public String getText() {
        return text;
    }
}
````
This is just an event that is able to carry its source and a text message from one bean to another.

## Creating the Core App

This application simply is the standard demo, with a dependency to our API.

Our **_DemoView_** will now not contain any UI itself: it is just composed by plugins:

````java
@Route("demo")
public class DemoView extends Div {

    private DemoView(@Plugin(directory = "./cotton-flow-demo-viewa/target", pluginId = "ViewA") Component a,
                     @Plugin(directory = "./cotton-flow-demo-viewb/target", pluginId = "ViewB") Component b) {
        add(a, b);
    }
}
````

**NOTE**: When this demo project is executed by an IDE like IntelliJ, the root of the running application will be the root directory of the project. For simplification, the plugin directories are pointing to the target folder of the plugin's Maven target directories here. In a real application, the directory would be some directory on the frontend server that the plugins are deployed into.

Also note that the injected plugins are just identified by the **_Component_** class; the real implementation type is unknown to the core.

## Creating the Plugins

The plugins ViewA and ViewB are roughly similar; we just exchange the A in the name's to B, so its enough to demonstrate how the project for the plugin A is set up.

First, we set up the POM for the project:

````xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.mantledillusion.vaadin</groupId>
        <artifactId>cotton-flow-demo-parent</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>cotton-flow-demo-viewb</artifactId>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>com.mantledillusion.vaadin</groupId>
            <artifactId>cotton-flow-demo-api</artifactId>
            <version>${project.version}</version>
            <scope>provided</scope>
        </dependency>

        <!-- com.mantledillusion dependencies -->
        <dependency>
            <groupId>com.mantledillusion.vaadin</groupId>
            <artifactId>cotton</artifactId>
            <version>2.0.0.ALPHA7</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>
        <finalName>ViewB</finalName>
    </build>
</project>
````

We need depencies to **_Cotton_** and obviously to our API; but in the running application, both will be provided by the frontends core, so they are not required to be included into the JAR.

Also, we set a fixed name for our JAR, so it can be found easily by the core.

Now we can create our plugin view:

````java
public class ViewA extends Div {

    private TextField textField;

    public ViewA(@Inject Bus bus) {
        this.textField = new TextField("View A");
        this.textField.addKeyPressListener(Key.ENTER, event -> bus.publish(
                new ApiEvent(ViewA.this, this.textField.getValue())));
        add(this.textField);
    }

    @Subscribe
    private void receive(ApiEvent event) {
        if (event.getSource() != this) {
            this.textField.setValue(event.getSource().getClass().getSimpleName()+" says: "+event.getText());
        }
    }
}
````

We inject **_Hura_**'s event bus. On ENTER key press in our view (that is only composed of a **_TextField_**), we publish the API's event using that bus.

Also we provide a method that is able to receive the API event; when an event comes in that is not from the view itself, the view uses its **_TestField_** to display what that other source has sent.

When the plugin is loaded by the core, the core has to be able to find what class to use for injection. **_Hura_** uses the specification for Java SPI META-INF service files to determine what classes from a plugin can be injected. To follow that specification, we create a file with the name **com.vaadin.flow.component.Component** (which is the fully qualified class name the plugin provides an implementation for):
````text
com.mantledillusion.vaadin.cotton.demo.ViewA
````
When Hura loads the plugin, it will find that file, notice that it contains implementations of **_Component_**, and will load **_ViewA_** from it.