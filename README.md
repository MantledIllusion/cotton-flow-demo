# Chapter 1.b: Hura WebLaunch Setup

Since Cotton uses [Hura](https://github.com/MantledIllusion/hura) 2's core for injection, it can easily be combined with Hura WebLaunch, which extends Hura Web with [Undertow](https://github.com/undertow-io/undertow), a lightweight high-performance embedded application server.

This setup provides lightning fast application startup while packing a low profile .JAR with embedded application server.

## Prepare the pom.xml

To use Cotton and Hura WebLaunch, simply add the respective dependencies it to your Maven POM:

````xml
<dependencies>
    <dependency>
        <groupId>com.mantledillusion.vaadin</groupId>
        <artifactId>cotton</artifactId>
        <version>2.0.0.ALPHA3</version>
    </dependency>
    <dependency>
        <groupId>com.mantledillusion.injection</groupId>
        <artifactId>hura-weblaunch</artifactId>
        <version>2.0.0.ALPHA3</version>
    </dependency>
</dependencies>
````

We are building an application that is packaged as a runnable jar with an embedded application server, so we have to instruct Maven to package it correctly using the **_maven-shade-plugin_** during the package phase:

````xml
<packaging>jar</packaging>

...

<build>
    <finalName>app</finalName>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.2.1</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <transformers>
                    <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                        <mainClass>com.mantledillusion.vaadin.cotton.demo.DemoApplication</mainClass>
                    </transformer>
                </transformers>
            </configuration>
        </plugin>
    </plugins>
</build>
````

Note that we have already pointed out the class of our application containing the _main()_ method that starts up the embedded server.

## Set up the Application's Initializer

Hura Web supplies the interface _**HuraWebApplicationInitializer**_ which can be used to start up Hura Web applications by defining the web environment in it. For Hura, this interface is a specialized _**Blueprint**_, so the web environment can be defined by adding methods annotated with _@Define_ to it.

To make Hura deploy Cotton, we create the initializer class **_DemoApplicationInitializer_** simply _@Define_ the _**CottonServlet**_ in it:
````java
@Define
public SingletonAllocation defineCottonServlet() {
    return WebEnvironmentFactory
        .registerServlet(CottonServlet.class)
        .addMapping("/*")
        .build();
}
````

We have also mapped the servlet to _"/*"_, which will cause all requests of our application to be given to our _**CottonServlet**_ for handling.

Since we are going to use an embedded server, we will not benefit from any Servlet-API mechanisms, so we will instruct Cotton to automatically discover all views annotated with _@Route_ and register them to Vaadin's **_Router_**:

````java
@Define
public PropertyAllocation defineActivateAutomaticRouteDiscovery() {
    return CottonEnvironment.forAutomaticRouteDiscovery(true);
}
````

## Set up the Application's Starter

If we want the runnable **.jar** to work, we need to create a class with a main() method that can be used for startup; we do this by creating the **_DemoApplication_** we have already specified in our **.pom**:

````java
public class DemoApplication {

    private static final ResourceManager VAADIN_RESOURCE_MANAGER =
            new ClassPathResourceManager(DemoApplication.class.getClassLoader(), "META-INF/resources/");

    public static void main(String[] args) {
        HuraWeblaunchApplication
                .build(DemoApplicationInitializer.class)
                .setResourceManager(VAADIN_RESOURCE_MANAGER)
                .startUp(args);
    }
}
````

Note that we have setup a resource manager; we need this to instruct **_Undertow_** to deliver any resources to a client browser that **_Vaadin_** might have, from JavaScript over CSS to images.

## Create a View

This chapter is just a deployment demonstration, so we do not care about the content of the view that much; it can be anything that is a Vaadin **_Component_** implementation.

The only important thing is that our view **_DemoView_** is annotated with _@Route_:

````java
@Route("demo")
public class DemoView extends VerticalLayout {
    ...
}
````