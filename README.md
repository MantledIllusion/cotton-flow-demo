# Cotton Flow Demo

This repository supplies a set of tutorials to present the main features of [Cotton 2](https://github.com/MantledIllusion/cotton-flow), which is an extension to Vaadin Flow.

The tutorials are divided into logical chapters with multiple tutorials each. Each tutorial has its own branch, so its code can be checked out individually.

The **README.md** of at the root of each branch will provide a step-by-step tutorial through each lesson. Note that from chapter 2 on, a Hura WebLaunch Setup (see chapter 1.b) from the master branch which is stripped down to be launched from an IDE is used for all the tutorials.

## Chapter 1: Cotton Flow Setups

Since Vaadin's Java API is based on the Servlet-API and Cotton is a mere extension of that Java API, Cotton can be run in all Servlet-API compatible environments.

The tutorials of this chapter provide several ways to build, run and deploy a Cotton-based Vaadin Flow application.

### 1.a: [Hura Web Setup](https://github.com/MantledIllusion/cotton-flow-demo/tree/01/a/hura_web_setup)

Since Cotton uses [Hura](https://github.com/MantledIllusion/hura) 2's core for injection, it can easily be combined with Hura Web, which provides an injected, Servlet-API based application environment.

It is ideal for adding extremely light-weight application environment injection when building a .WAR to deploy on application servers.

### 1.b: [Hura WebLaunch Setup](https://github.com/MantledIllusion/cotton-flow-demo/tree/01/b/hura_weblaunch_setup)

Since Cotton uses [Hura](https://github.com/MantledIllusion/hura) 2's core for injection, it can easily be combined with Hura WebLaunch, which extends Hura Web with [Undertow](https://github.com/undertow-io/undertow), a lightweight high-performance embedded application server.

This setup provides lightning fast application startup while packing a low profile .JAR with embedded application server.

### 1.c: [Spring WebMVC Setup](https://github.com/MantledIllusion/cotton-flow-demo/tree/01/c/spring_webmvc_setup)

Spring is the all-time favorite for building feature-heavy applications fast.

In combination with [Spring-WebMVC](https://github.com/spring-projects/spring-framework), Cotton can be build into a Spring environment .WAR, where Cotton is responsible for view injection while Spring injects environment beans like web service endpoints and database connectors.

### 1.d: [Spring-Boot Setup](https://github.com/MantledIllusion/cotton-flow-demo/tree/01/d/spring_boot_setup)

Spring is the all-time favorite for building feature-heavy applications fast.

In combination with [Spring-Boot](https://github.com/spring-projects/spring-boot), Cotton can be build into a Spring environment .JAR, where Cotton is responsible for view injection while Spring provides an embedded webserver and injects environment beans like web service endpoints and database connectors.

### 1.e: [Native Setup](https://github.com/MantledIllusion/cotton-flow-demo/tree/01/e/native_setup)

Vaadin uses Servlet-API; so a Cotton application can be build as a simple .WAR, without any strings attached.

## Chapter 2: Configuring the Environment

One thing is your application, the other is the environment it runs in. Whether its properties, singletons or security, all those things mark the boundaries your application grows in. Cotton offers several possibilities to configure that environment easily, so you can concentrate on building your application.

### 2.a: [Automatic Route Discovery](https://github.com/MantledIllusion/cotton-flow-demo/tree/02/a/automatic_route_discovery)

Vaadin maps URLs to views with **_Router_**, where all visitable views are registered. 

When using external application servers, Servlet-API mechanisms will help Vaadin register components annotated with _@Route_ automatically.

When using an embedded one, you can delegate that job to Cotton.

### 2.b: [Defining Application-Level (Singleton) Beans](https://github.com/MantledIllusion/cotton-flow-demo/tree/02/b/define_app_level_beans)

Hura is responsible for injecting every kind of bean from the **_CottonServlet_** downward. Or put differently: no matter what setup is used, Hura injects all beans that have a session based lifecycle.

But most applications require beans whose lifecycle begins with the application starting up and does not end before the app shuts down: application level singleton beans.

That are beans like service endpoints, database controllers and so on. Their lifecycle is not bound to Cotton, but they need to be injected into beans whose lifecycle is. For that purpose, app-level singletons can be pre defined.

### 2.c: [Localization](https://github.com/MantledIllusion/cotton-flow-demo/tree/02/c/localization)

Adapting to a user's language is already important for desktop applications, but it is critical for the web since usually the whole world will have access to a site.

Vaadin Flow already comes with a set of features to fulfill this purpose, but Cotton is able to easily load translations into these features and to retrieve from them from every point in the application.

### 2.d: [Login & Access Restriction](https://github.com/MantledIllusion/cotton-flow-demo/tree/02/d/login_and_access_restriction)

The huge majority of applications require a login mechanism of some kind, either for access restriction and/or for determining which rights the current use owns.

Cotton offers a generic mechanisms that provide functionality for setting a current user and behaving differently according to that users rights.

### 2.e: [Consuming Metrics](https://github.com/MantledIllusion/cotton-flow-demo/tree/02/e/consuming_metrics)

Cotton implements the [TrailMetrics support for Vaadin Flow](https://github.com/MantledIllusion/trail-metrics/tree/master/trail-metrics-support-vaadin-flow) that enables dispatching session based metrics from anywhere in the application and even dispatches a set of general metrics itself.

If desired, all dispatched metrics can be consumed and then be used for any purpose desired.

## Chapter 3: Building Views

Vaadin is all about frontend, so its main concern is building views. Cotton provides a lot of assisting functionality for doing so, while remaining 100% compatible with native Vaadin functionality for situations when something specific has to be build.

### 3.a: [Component Factorizing](https://github.com/MantledIllusion/cotton-flow-demo/tree/03/a/component_factorizing)

Building UI in Vaadin requires creating and configuring lots and lots of UI components. Cotton contains a set of fluent component builders to cope with the repeated setter calling of components.

### 3.b: [The EventBus](https://github.com/MantledIllusion/cotton-flow-demo/tree/03/b/the_eventbus)

As frontends grow, there sooner or later will always be a need for sending messages between separated parts of the application. To prevent class dependency hell, it is better to decouple those parts since they do not need know each other, but make them exchange messages anonymously via event bus.

The injection framework Hura which is included in Cotton offers a build-in event bus, where subscribing beans to that bus are automatically hooked on and off with their life cycle. Cotton pre-configures that bus, so no events of session beans can leave their own session, making it impossible to mistakenly alert bus subscribers that are not meant to be contacted.

### 3.c: [Presenting Views](https://github.com/MantledIllusion/cotton-flow-demo/tree/03/c/presenting_views)

The MVP (Model/View/Presenter) pattern is a popular architectural pattern that can be applied to basically all types of frontends.

Cotton supports MVP by providing functionality of creating and hooking a presenter to a view, without allowing the view to be able to obtain a reference to that presenter.

### 3.d: [Building a Plugin UI](https://github.com/MantledIllusion/cotton-flow-demo/tree/03/d/building_a_plugin_ui)

In some cases it might be a great idea so take modularization of a frontend to the next level; by linking together the parts as separated plugins. 

One example of such a situation might be the frontend for a _Service Oriented Architecture_ (SOA). With the application cleanly separated (for example into services for customer, order and product using _Domain Driven Design_), changes to those services could be deployed at any time without affecting the rest of the platform.

But with a monolith frontend, every API changing service deployment would have the effect of the complete frontend having to be redeployed as well. Using a plugin based frontend (with the single plugins of the frontend shaped just as the service), it would be possible to keep the frontend up and running while just deploying an updated plugin for the service.

Since Cotton uses Hura 2, basic plugin functionality is already on board, ready to be used.

## Chapter 4: Data Binding

Since Vaadin 8, data binding in the framework has been overhauled to support Java 8's lambdas for a simpler use of Vaadin's Java API.

Cotton integrates the data modeling framework [Epiphy](https://github.com/MantledIllusion/epiphy) for easier Vaadin data binding when handling complex Java POJO object models.

The major advantage of binding to a complex model instead of single values is that exchanging a model's root can be done in a single operation, which enables to refresh the data in a whole UI in just one call.

### 4.a: [Deep Model Binding](https://github.com/MantledIllusion/cotton-flow-demo/tree/04/a/deep_model_binding)

The Epiphy framework enables access to properties that are deep down a model's object tree only while only working with the tree's root. Cotton uses this feature to bind Vaadin components implementing the _**HasValue**_ interface to data deep down in an object tree.

### 4.b [MVP-Compliant Data Binding](https://github.com/MantledIllusion/cotton-flow-demo/tree/04/b/mvp_compliant_data_binding)

Cotton's MVP functionality allows applications to be split into view and logic code cleanly; and that goes for the data binding as well, where the model should be controlled elsewhere than it is bound.