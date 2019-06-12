# Chapter 2.a: Automatic Route Discovery

Vaadin maps URLs to views with **_Router_**, where all visitable views are registered. 

When using external application servers, Servlet-API mechanisms will help Vaadin register components annotated with _@Route_ automatically.

When using an embedded one, you can delegate that job to Cotton.

## Enable Automatic Route Discovery

Using **_CottonEnvironment_**, the property that instructs **_Cotton_** to discover routes itself is defined easily:

````java
public class DemoApplicationInitializer implements HuraWebApplicationInitializer {

    ...

    @Define
    public PropertyAllocation defineActivateAutomaticRouteDiscovery() {
        return CottonEnvironment.forAutomaticRouteDiscovery(true);
    }
}
````

## Set the Base Package of the Discovery

When **_Cotton_** is in charge of route discovery, it will use a base package to start from to prevent having to search through every single class of the class path.

When the **_CottonServlet_** is used in an **_Hura Web_** or **_Hura WebLaunch_** environment, the default base package for the discovery is the base package of the **_Hura_** application.

In all other cases, the default base package is the package of the **_Blueprint_** given to the **_CottonServlet_**.

Either way, it can be overridden using the **_CottonEnvironment_**:

````java
@Define
public PropertyAllocation defineAutomaticRouteDiscoveryBasePackage() {
    return CottonEnvironment.forApplicationBasePackage(DemoView.class.getPackage());
}
````