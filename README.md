# Chapter 1.e Native Setup

Vaadin uses Servlet-API; so a Cotton application can be build as a simple .WAR, without any strings attached.

## Prepare the pom.xml

To use Cotton simply add its dependency it to your Maven POM:

````xml
<dependencies>
    <dependency>
        <groupId>com.mantledillusion.vaadin</groupId>
        <artifactId>cotton</artifactId>
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

## Create a Servlet-API compliant Cotton Servlet

Our application will run on an external application server that supports the Servlet-API. Since there is no framework handling injection outside of Vaadin, we can let the application server take care of instantiating our servlet.

To do that, we extend **_CottonServlet_** with our own class **_DemoServlet_** that is able to manually retrieve a **_Blueprint_** instance via constructor:

````java
@WebServlet("/*")
public class DemoServlet extends CottonServlet {

    @Autowired
    public DemoServlet(Blueprint cottonEnvironment) {
        super(cottonEnvironment);
    }
}
````

The _@WebServlet_ annotation will cause the application server to create an instance from our servlet and register it under the _"/*"_ path we specified.

From the Vaadin servlet down though, **_Hura_** will be responsible for injection. Since the standard **_CottonServlet_** is meant to be injected in a Hura application environment, we have to give it a **_Blueprint_** in situations when its not. For that purpose, we create a **_DemoEnvironment_** we can give to its constructor:

````java
public class DemoEnvironment implements Blueprint {
    
}
````

Currently that class is empty, but we can fill it by **_@Define_**-ing any beans we need in our whole **_Vaadin_** application, like services or similar.

## Create a View

This chapter is just a deployment demonstration, so we do not care about the content of the view that much; it can be anything that is a Vaadin **_Component_** implementation.

The only important thing is that our view **_DemoView_** is annotated with _@Route_:

````java
@Route("demo")
public class DemoView extends VerticalLayout {
    ...
}
````