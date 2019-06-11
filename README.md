# Chapter 1.a: Hura Web Setup

Since Cotton uses [Hura](https://github.com/MantledIllusion/hura) 2's core for injection, it can easily be combined with Hura Web, which provides an injected, Servlet-API based application environment.

It is ideal for adding extremely light-weight application environment injection when building a .WAR to deploy on application servers.

## 1) Prepare the pom.xml

To use Cotton and Hura Web, simply add the respective dependencies it to your Maven POM:

````xml
<dependencies>
    <dependency>
        <groupId>com.mantledillusion.vaadin</groupId>
        <artifactId>cotton</artifactId>
        <version>2.0.0.ALPHA3</version>
    </dependency>
    <dependency>
        <groupId>com.mantledillusion.injection</groupId>
        <artifactId>hura-web</artifactId>
        <version>2.0.0.ALPHA3</version>
    </dependency>
</dependencies>
````

We are building an application here that is deployable on an external application server, so we instruct maven to package the application as a **.war**:

````xml
<packaging>war</packaging>

...

<build>
    <finalName>app</finalName>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-war-plugin</artifactId>
            <version>3.2.2</version>
        </plugin>
    </plugins>
</build>
````

## 2) Set up the Application's Initializer

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

## 3) Create a View

This chapter is just a deployment demonstration, so we do not care about the content of the view that much; it can be anything that is a Vaadin **_Component_** implementation.

The only important thing is that our view **_DemoView_** is annotated with _@Route_:

````java
@Route("demo")
public class DemoView extends VerticalLayout {
    ...
}
````

In an environment of an external application server, all classes annotated with _@Route_ will be collected by Servlet-API mechanisms utilized by Vaadin, so there is no need for manually registering the view anywhere.
