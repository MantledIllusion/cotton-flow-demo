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

To restrict access to our **_DemoView_**, we simply annotate it:

````java
@Route("demo")
@Restricted("(right_a || right_b) && right_c")
public class DemoView extends Div {

    public DemoView() {
        Button b = new Button("Logout");
        b.addClickListener(event -> WebEnv.logOut());
        add(new HorizontalLayout(new Label("Accessed!"), b));
    }
}
````

When navigating to **_DemoView_** now, there has to be a user in the session that also possesses the right with the identifier "_right_c_", as well as either "_right_a_" or "_right_b_".

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

    private static final User USER = rightIds -> Arrays.asList("right_a", "right_c").containsAll(rightIds);

    public DemoLoginView() {
        setSizeFull();

        Button b = new Button("Login");
        b.addClickListener(event -> WebEnv.logIn(USER));
        add(b);
    }
}
````

Note that the User in this example is only a mock that simply mocks owning the rights "_right_a_" and "_right_c_", which will be enough for our **_DemoView_**.

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

The **_UserProvider_** can be defined in the environment **_Blueprint_** as **_LoginProvider_** like so:

````java
@Define
public SingletonAllocation defineLoginProvider() {
    return CottonEnvironment.forLoginProvider(LoginProvider.byUserProvider(new DemoUserProvider()));
}
````