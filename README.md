# Chapter 1.d: Spring-Boot Setup

Spring is the all-time favorite for building feature-heavy applications fast.

In combination with [Spring-Boot](https://github.com/spring-projects/spring-boot), Cotton can be build into a Spring environment .JAR, where Cotton is responsible for view injection while Spring provides an embedded webserver and injects environment beans like web service endpoints and database connectors.

## Prepare the pom.xml

To use Cotton and Spring Boot, simply add the respective dependencies it to your Maven POM:

````xml
<dependencies>
    <dependency>
        <groupId>com.mantledillusion.vaadin</groupId>
        <artifactId>cotton</artifactId>
        <version>2.0.0.ALPHA3</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>2.1.4.RELEASE</version>
    </dependency>
</dependencies>
````

We are building an application that is packaged as a runnable jar with an embedded application server, so we have to use the **spring-boot-plugin** that will package it accordingly for us:

````xml
<packaging>jar</packaging>

...

<build>
    <finalName>app</finalName>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <version>2.1.4.RELEASE</version>
            <executions>
                <execution>
                    <goals>
                        <goal>repackage</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
````

## Set up the Application's Initializer

### Create a Spring managed Cotton Servlet

Although **_Spring_** will handle the injection of all application beans, **_Hura_** will still be responsible for all beans in the context of **_Vaadin_**. Since the standard **_CottonServlet_** is meant to be injected in a Hura application environment, we have to give it a **_Blueprint_** in situations when its not.

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

The _@WebServlet_ annotation will cause **_Spring_** to create a bean from our servlet and register it under the _"/*"_ path we specified.

Since the constructor is autowired, Spring will find a bean implementing **_Blueprint_** that will be used by the servlet for the configuration of **_Hura_**.

### Create a Spring Config

Using a **_Spring_** config, you can define all beans that **_Spring_** has to manage and since we need it to manage our **_DemoServlet_** as well, we create a **_SpringBootConfig_** where we can define it:

````java
@Configuration
@ServletComponentScan
public class SpringBootConfig implements Blueprint {

    @Define
    public PropertyAllocation defineActivateAutomaticRouteDiscovery() {
        return CottonEnvironment.forAutomaticRouteDiscovery(true);
    }
}
````

The _@ServletConfiguration_ annotation will cause Spring to find and register our **_DemoServlet_**.

Since we are going to use an embedded server, we will not benefit from any Servlet-API mechanisms, so we will instruct **_Cotton_** to automatically discover all views annotated with _@Route_ and register them to **_Vaadin_**'s **_Router_**.

Also note how the **_SpringBootConfig_** implements **_Blueprint_**, which will cause it being given to **_DemoServlet_**'s constructor; this will enable us to _@Define_ methods for the bean environment of **_Hura_** that are also able to return **_Spring_** beans to be taken from one bean pool to another. For example, we could create a _**RestTemplate**_ returning method that is both annotated with _@Bean_ and _@Define_, so the bean could be injected into any **_Cotton_** managed view.

### Create a Spring Initializer

If we want the runnable **.jar** to work, we need to create a class with a main() method that can be used for startup; we do this by creating a **_SpringApplicationStarter_**:

````java
@SpringBootApplication
public class SpringApplicationStarter {

    public static void main(String[] args) {
        SpringApplication.run(SpringApplicationStarter.class, args);
    }
}
````

The _@SpringBootApplication_ annotation will cause **_Spring_** to search for configurations in the same package, so it will find our **_SpringBootConfig_**.

## Create a View

This chapter is just a deployment demonstration, so we do not care about the content of the view that much; it can be anything that is a Vaadin **_Component_** implementation.

The only important thing is that our view **_DemoView_** is annotated with _@Route_:

````java
@Route("demo")
public class DemoView extends VerticalLayout {
    ...
}
````