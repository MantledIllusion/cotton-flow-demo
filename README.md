# Chapter 1.c Spring WebMVC Setup

Spring is the all-time favorite for building feature-heavy applications fast.

In combination with [Spring-WebMVC](https://github.com/spring-projects/spring-framework), Cotton can be build into a Spring environment .WAR, where Cotton is responsible for view injection while Spring injects environment beans like web service endpoints and database connectors.

## Prepare the pom.xml

To use Cotton and Spring MVC, simply add the respective dependencies it to your Maven POM:

````xml
<dependencies>
    <dependency>
        <groupId>com.mantledillusion.vaadin</groupId>
        <artifactId>cotton</artifactId>
        <version>2.0.0.ALPHA3</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>5.1.6.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-validator</artifactId>
        <version>5.2.4.Final</version>
    </dependency>
</dependencies>
````

We also added **hibernate-validator** because **_Spring_** requires a Javax validation provider on the classpath.

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

## Set up the Application's Initializer

### Create a Spring managed Cotton Servlet

Although **_Spring_** will handle the injection of all application beans, **_Hura_** will still be responsible for all beans in the context of **_Vaadin_**. Since the standard **_CottonServlet_** is meant to be injected in a Hura application environment, we have to give it a **_Blueprint_** in situations when its not.

To do that, we extend **_CottonServlet_** with our own class **_DemoServlet_** that is able to manually retrieve a **_Blueprint_** instance via constructor:

````java
public class DemoServlet extends CottonServlet {

    public DemoServlet(Blueprint environment) {
        super(environment);
    }
}
````

### Create a Spring Config

Using a **_Spring_** config, you can define all beans that **_Spring_** has to manage and since we need it to manage our DemoServlet as well, we create a **_SpringWebConfig_** where we can define it:

````java
@Configuration
public class SpringWebConfig implements Blueprint {

    @Bean
    public HandlerAdapter handlerAdapter() {
        return new SimpleServletHandlerAdapter();
    }

    @Bean
    public HandlerMapping handlerMapping(@Autowired Servlet demoServlet) {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        Map<String, Object> urls = new HashMap<>();
        urls.put("/*", demoServlet);
        mapping.setUrlMap(urls);
        return mapping;
    }

    @Bean
    public Servlet vaadinServlet() {
        return new DemoServlet(this);
    }

    @Bean
    public SimpleServletPostProcessor simpleServletPostProcessor() {
        return new SimpleServletPostProcessor();
    }
}
````

Beside the servlet and a handlermapping for it, **_Spring MVC_** needs the additional beans **_HandlerAdapter_** and **_SimpleServletPostProcessor_** to be able to handle requests accordingly.

Also note how the **_SpringWebConfig_** implements **_Blueprint_** and is given to our **_DemoServlet_**; this will enable us to _@Define_ methods for the bean environment of **_Hura_** that are also able to return **_Spring_** beans to be taken from one bean pool to another. For example, we could create a _**RestTemplate**_ returning method that is both annotated with _@Bean_ and _@Define_, so the bean could be injected into any **_Cotton_** managed view.

### Create a Spring Initializer

Spring MVC supplies the _**AbstractAnnotationConfigDispatcherServletInitializer**_ which can be used to define the root classes **_Spring_** can create a context out of.

We create override that class and specify the class we would like to configure servlets with:
````java
public class SpringApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {SpringWebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/*"};
    }
}
````

We have also mapped the servlets to _"/*"_, which will cause all requests of our application to be given to our _**CottonServlet**_ for handling.

## Create a View

This chapter is just a deployment demonstration, so we do not care about the content of the view that much; it can be anything that is a Vaadin **_Component_** implementation.

The only important thing is that our view **_DemoView_** is annotated with _@Route_:

````java
@Route("demo")
public class DemoView extends VerticalLayout {
    ...
}
````

In an environment of an external application server, all classes annotated with _@Route_ will be collected by Servlet-API mechanisms utilized by Vaadin, so there is no need for manually registering the view anywhere.