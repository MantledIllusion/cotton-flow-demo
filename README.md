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

One thing is your application, the other is the environment it runs in. Whether its properties, singletons or security, all those things are things mark the boundaries your application grows in. Cotton offers several possibilities to configure that environment easily, so you can concentrate on building your application.

### 2.a: [Automatic Route Discovery](https://github.com/MantledIllusion/cotton-flow-demo/tree/02/a/automatic_route_discovery)

Vaadin maps URLs to views with **_Router_**, where all visitable views are registered. 

When using external application servers, Servlet-API mechanisms will help Vaadin register components annotated with _@Route_ automatically.

When using an embedded one, you can delegate that job to Cotton.

### 2.b [Defining Application-Level (Singleton) Beans](https://github.com/MantledIllusion/cotton-flow-demo/tree/02/b/define_app_level_beans)

Hura is responsible for injecting every kind of bean from the **_CottonServlet_** downward. Or put differently: no matter what setup is used, Hura injects all beans that have a session based lifecycle.

But most applications require beans whose lifecycle begins with the application starting up and does not end before the app shuts down: application level singleton beans.

That are beans like service endpoints, database controllers and so on. Their lifecycle is not bound to Cotton, but they need to be injected into beans whose lifecycle is. For that purpose, app-level singletons can be pre defined.