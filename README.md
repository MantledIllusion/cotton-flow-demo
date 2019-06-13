# Chapter 2.b: Defining Application-Level (Singleton) Beans

Hura is responsible for injecting every kind of bean from the **_CottonServlet_** downward. Or put differently: no matter what setup is used, Hura injects all beans that have a session based lifecycle.

But most applications require beans whose lifecycle begins with the application starting up and does not end before the app shuts down: application level singleton beans.

That are beans like service endpoints, database controllers and so on. Their lifecycle is not bound to Cotton, but they need to be injected into beans whose lifecycle is. For that purpose, app-level singletons can be pre defined.

## Where to define App-Level Beans

App-level singletons are such beans that are already available to the **_CottonServlet_** when it is instantiated.

Depending on the setup, the servlet will receive them in 2 different ways:
- In **_Hura Web(Launch)_** setups, the **_CottonServlet_** will be injected by **_Hura_**, so the servlet will receive app-level beans from its injection's context. That context in turn receives its beans from the initializer Blueprint.
- In all other setups, a **_Blueprint_** instance has to be given to the servlet's constructor, which will serve as the source of app-level beans.

In either way, a **_Blueprint_** exists that app-level beans can be defined in.

## How to define App-Level Beans

In the correct **_Blueprint_** simply create a _@Define_-annotated method returning a **_SingletonAllocation_**:

````java
public static final String QUALIFIER_APPLVL_BEAN = "appLvlBeanQualifier";

@Define SingletonAllocation defineAppLevelSingleton() {
    return SingletonAllocation.of(QUALIFIER_APPLVL_BEAN, Optional.of("someString"));
}
````

Since a singleton in **_Hura_** always has a qualifier, we give it one we will use to refer to when injecting the bean.

## How to inject App-Level Beans

Injecting app-level beans in **_Hura_** works like injecting any other singleton; in fact, **_Hura_** does not see any difference to other singletons, since "_app-level_" only means "_from the highest sequence of the injection tree_" for the framework.

So in our view (which is a navigation-level bean), we can simply inject it using the same qualifier:

````java
@Route("demo")
public class DemoView extends Div {

    public DemoView(@Inject @Qualifier(DemoApplicationInitializer.QUALIFIER_APPLVL_BEAN) Optional<String> appLvlBean) {
        add(new Label(appLvlBean.get()));
    }
}
````

Without specifying the _@Qualifier_ annotation, **_Hura_** would recognize the injection as independent bean instead of singleton and would try to instantiate a new optional. 

If we were using a qualifier of a singleton **_Hura_** does not know yet, it would try to instantiate a new optional as well, just not as independent bean, but as a new navigation level singleton with that qualifier.

The difference between independent and singleton beans as well as injection sequence levels are the basics of what injection with **_Hura_** has to offer; for more details head to the [framework's github page](https://github.com/MantledIllusion/hura).