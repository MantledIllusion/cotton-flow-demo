# Chapter 2.d: Login & Access Restriction

The huge majority of applications require a login mechanism of some kind, either for access restriction and/or for determining which rights the current use owns.

Cotton offers a generic mechanisms that provide functionality for setting a current user and behaving differently according to that users rights.

## The Cotton User

**_Cotton_** only has a very broad concept of a user; it is an entity that can either exist in a session or not and might have 0&#8594;n rights embodied by string identifiers.

For this purpose **_Cotton_** offers the interface **_User_**, which just contains 1 method receiving a set of such right identifiers. The method then has to check whether the **_User_** own all of the rights of the given identifiers and return back a boolean value.

## Manually Logging a User in/out

As being said, a session in **_Cotton_** can either contain one or none **_User_** instance. 

The **_WebEnv_** allows manual access to that instance as well as allowing to either set (log in) or remove (log out) the current **_User_**:

````java
WebEnv.isLoggedIn();
WebEnv.getLoggedInUser();
WebEnv.logIn(user);
WebEnv.logOut();
````

## Restricting access to a Route

**_Cotton_** offers the _@Restricted_ annotation that can be used on any **_Component_** that is a direct root navigation target, for example by being annotated with _@Route_.

When a navigation target is annotated with _@Restricted_, **_Cotton_** will automatically check the session's current **_User_** to exist. If there are right identifiers given to the annotation, **_Cotton_** will also check that the **_User_** currently logged in possesses these rights before navigating.

To restrict access to a **_RightAorBWithCView_**, we simply annotate it:

````java
@Route("demo")
@Restricted("(right_a || right_b) && right_c")
public class RightAorBWithCView extends Div {

    public RightAorBWithCView() {
        Button b = new Button("Logout");
        b.addClickListener(event -> WebEnv.logOut());
        add(new VerticalLayout(new Label("Accessed with right A or B, but definitely with c!"), b));
    }
}
````

When navigating to **_RightABCView_** now, there has to be a user in the session that also possesses the right with the identifier "_right_c_", as well as either "_right_a_" or "_right_b_".

The expression in the _@Restricted_ annotation could also be just the id of one right, no right or even a way more complex boolean syntax as used here; it is parsed as a logical **_Expression_**.

**Note that the _@Restricted_ annotation only works on the top level navigation target; it has no effect on any sub views a navigation target might have!**

## Automatic Login

When a visitor navigates to a restricted navigation target but is not logged in as a **_User_**, **_Cotton_** will have to deny access by causing a **HTTP403** error.

Most of the time the visitor will actually be a person that is already registered as a user, but simply is not logged in _yet_.

For this scenario, **_Cotton_** can automatically cause a login process instead of causing a premature **HTTP403** error by using an implementation of the **_LoginProvider_** class.

### Automatically log in by View

When a not logged in visitor tries to access a restricted navigation target, **_Cotton_** can redirect to a login view first where that visitor can either credentials for authentication.

That view can be any **_Component_** and is expected to call the _**WebEnv**.logIn()_ when the visitor has entered credentials. 

````java
@Route("login")
public class DemoLoginView extends VerticalLayout {

    public DemoLoginView() {
        setSizeFull();
        CheckboxGroup<String> rightGroup = new CheckboxGroup<>();
        rightGroup.setItems("right_a", "right_b", "right_c");
        add(rightGroup);

        Button b = new Button("Login");
        b.addClickListener(event -> WebEnv.logIn(requiredRights -> rightGroup.getSelectedItems().containsAll(requiredRights)));
        add(b);
    }
}
````

Note that the User in this example is only a mock simply mocking owning the rights selected, which will be perfect for testing the right constellations of our views.

Calling _**WebEnv**.login()_ will trigger a **_Page_** reload. Since the login view was only rerouted to, that reload will cause the restricted view to be visited again.

The login view can be defined in the environment **_Blueprint_** as **_LoginProvider_** like so:

````java
@Define
public SingletonAllocation defineLoginProvider() {
    return CottonEnvironment.forLoginProvider(LoginProvider.byView(DemoLoginView.class));
}
````

### Automatically log in by UserProvider

Not all applications have a login view themselves; for example, some applications might user a single sign-on service.

For these cases **_Cotton_** offers to specify an implementation of the **_UserProvider_** interface as the **_LoginProvider_**. That instance will be called when an automatic login is triggered and might cause any action necessary. 

For example, it could check the query parameters for an authentication token;
- If there is none, redirect to the login mask of the single sign-on service
- If there is one, call the webservice single sign-on service to validate it and return a **_User_** instance

In another example, when using **Cotton** in conjugation with **Spring Security**, the **_UserProvider_** could simply access _**SecurityContextHolder**.getContext().getAuthentication()_ and would retrieve the current **_Thread_**'s **_Authentication_** object that could be converted into a **_User_**.

The **_UserProvider_** can be defined in the environment **_Blueprint_** as **_LoginProvider_** like so:

````java
@Define
public SingletonAllocation defineLoginProvider() {
    return CottonEnvironment.forLoginProvider(LoginProvider.byUserProvider(new DemoUserProvider()));
}
````

## Right Based Forwarding

Depending on the application's view structure, it is sometimes desired to map a broader path to a specific sub-path; for example, ".../" should redirect to ".../welcome". In classic **Vaadin**, the annotation _@RouteAlias_ is used for this task.

The _@RouteAlias_ annotation has a major downside though: the application is not allowed to have multiple views that map onto the same alias path, which might be desirable in case that the forwarding decision should be based onto the current **_User_**'s authentication and authorization context.

As a result, Cotton offers the _@PrioritizedRouteAlias_ annotation, that works hand in hand with the _@Route_ and _@Restricted_ annotation and allows multiple views to map onto the same path. Out of all the views mapped, the view with the lowest priority is picked whose authentication and authorization requirements are met.

To test this, we first change the routing of our **_RightAorBwithCView_**:

```java
@Route("demo/rightAorBwithC")
@PrioritizedRouteAlias(value = "demo", priority = 0)
@Restricted("(right_a || right_b) && right_c")
public class RightAorBWithCView extends Div {
    ...
}
```

Now, "demo" is just a base path and not the real path of the view anymore, but the view still maps to it using the alias.

On top of this, we now add two more views that map to "demo":

```java
@Route("demo/rightAndB")
@PrioritizedRouteAlias(value = "demo", priority = 1)
@Restricted("right_a && right_b")
public class RightAandBView extends Div {

    public RightAandBView() {
        Button b = new Button("Logout");
        b.addClickListener(event -> WebEnv.logOut());
        add(new VerticalLayout(new Label("Accessed with right A and B!"), b));
    }
}
```

```java
@Route("demo/rightAorC")
@PrioritizedRouteAlias(value = "demo", priority = 2)
@Restricted("right_a || right_c")
public class RightAorCView extends Div {

    public RightAorCView() {
        Button b = new Button("Logout");
        b.addClickListener(event -> WebEnv.logOut());
        add(new VerticalLayout(new Label("Accessed with right A or C!"), b));
    }
}
```

All of these views still require login, so the login view is still shown when accessing the "demo" path. Depending on the selected rights though, **Cotton** will forward the **_User_** to either of those three views.

If multiple of the views match the owned rights, the one with the lower priority is used. When none matches, the usual HTTP403 is thrown.