# Chapter 4.c: Right Dependent Data Binding

As business applications grow, they often become used by more than one type of user. As soon as there is more than one user group, the need will arise to restrict data access depending on the different groups, often upto field precision.

Cotton allows specifying the access mode per-field in the data binding, effectively preventing data from being modified (or even seen) by users without sufficient rights.

## Setting up a Model

For this lesson we do not need a complex model, we will just create one with two **_String_** fields, so we can differentiate the access to them:

```java
public class Model {

    private String sensitiveField;
    private String superSensitiveField;

    public String getSensitiveField() {
        return sensitiveField;
    }

    public void setSensitiveField(String sensitiveField) {
        this.sensitiveField = sensitiveField;
    }

    public String getSuperSensitiveField() {
        return superSensitiveField;
    }

    public void setSuperSensitiveField(String superSensitiveField) {
        this.superSensitiveField = superSensitiveField;
    }
}
```

## Setting up a User

Since we are depending on access rights here, we require a **_User_** that can be checked for rights.

There are 2 different right types for the different levels of data sensitivity, so we each create a user for them and also a guest user that does not have any right:

```java
public class DemoUser implements User {

    static final String RIGHT_SUPER_SENSITIVE_DATA = "_admin";
    static final String RIGHT_SENSITIVE_DATA = "_casual";

    static final User USER_POWER = new DemoUser(RIGHT_SUPER_SENSITIVE_DATA);
    static final User USER_CASUAL = new DemoUser(RIGHT_SENSITIVE_DATA);
    static final User USER_GUEST = new DemoUser();

    private final Set<String> rightIds;

    private DemoUser(String... rightIds) {
        this.rightIds = new HashSet<>(Arrays.asList(rightIds));
    }

    @Override
    public boolean hasRights(Set<String> rightIds) {
        return this.rightIds.containsAll(rightIds);
    }
}
```

## Setting up the View

In our view, we will first setup the properties we need to access the **_Model_**'s data:

```java
@Route("demo")
public class DemoView extends VerticalLayout {

    private static final ModelProperty<Model, String> SENSITIVE_PROPERTY = 
            ModelProperty.fromObject(Model::getSensitiveField, Model::setSensitiveField);
    private static final ModelProperty<Model, String> SUPER_SENSITIVE_PROPERTY = 
            ModelProperty.fromObject(Model::getSuperSensitiveField, Model::setSuperSensitiveField);
    
    ...
}
```

In the constructor, we inject ourselves a **_ModelAccessor_** and set a **_Model_** instance to it:

```java
@Route("demo")
public class DemoView extends VerticalLayout {

    ...

    public DemoView(@Inject ModelAccessor<Model> accessor) {
        Model model = new Model();
        model.setSensitiveField("Some sensitive value");
        model.setSuperSensitiveField("Some super sensitive value");
        accessor.setModel(model);

        ...
    }
}
```

Next we create a button bar which is able to login each of our 3 users, or log the current user out:

```java
@Route("demo")
public class DemoView extends VerticalLayout {

    ...

    public DemoView(@Inject ModelAccessor<Model> accessor) {
        
        ...

        add(HorizontalLayoutBuilder.create().
                add(ButtonBuilder.create().
                        setText("Login Power User").
                        addClickListener(e -> WebEnv.logIn(DemoUser.USER_POWER)).
                        build()).
                add(ButtonBuilder.create().
                        setText("Login Casual User").
                        addClickListener(e -> WebEnv.logIn(DemoUser.USER_CASUAL)).
                        build()).
                add(ButtonBuilder.create().
                        setText("Login Guest User").
                        addClickListener(e -> WebEnv.logIn(DemoUser.USER_GUEST)).
                        build()).
                add(ButtonBuilder.create().
                        setText("Logout").
                        addClickListener(e -> WebEnv.logOut()).
                        build()).
                build());

        ...
    }
}
```

Now comes the fun part, where we use the **_ModelAccessor_** to create restricted bindings.

When no restriction is created at all, **Cotton** will automatically assume a binding to be non-threatening, so it will allow full read/write access to it. As soon as at least one restriction is created, this logic is turned around: everything is forbidden if there is not a restriction explicitly allowing it.

For our example, we want to hide the fields of both of our 2 properties' when no user is logged in. To a user without any right, we want to show that there are fields, but would not want to show their content, because the data is sensitive. Since these are restrictions for all fields we want to bind, we can set them directly on our **_ModelAccessor_**:

```java
@Route("demo")
public class DemoView extends VerticalLayout {
    
    ...

    public DemoView(@Inject ModelAccessor<Model> accessor) {
        
        ...
        
        accessor.setAuditMode(Binding.AuditMode.RESTRICTIVE);
        accessor.setAudit(Binding.AccessMode.MASKED, true);
        
        ...
}
```

Setting the audit mode as **_AuditMode_.RESTRICTIVE** will cause all fields to be fully restricted by default, and we ourselves will allow more access when using _.setAudit()_ individually. The opposite mode, which is **_AuditMode_.GENEROUS** and is also the default, allows full access from which individual restrictions have to be set.

Since we set the **_AccessMode_.MASKED** and demand the user to be logged in for it by providing _true_ to the method, non-logged in users will fall back into **_AuditMode_.RESTRICTIVE**, which will set **_AccessMode_.HIDDEN**. If we would provide _false_, the base **_AccessMode_** for <u>every</u> **_User_** would become **_AccessMode_.MASKED**.

The sensitivity of the 2 of our **_Model_**'s fields are different, so we have to set their restrictions individually. We can do so by setting them in a builder-fashion just when building and binding the fields, by using the _bindAndConfigure()_ methods of our **_ComponentBuilder_**:

```java
@Route("demo")
public class DemoView extends VerticalLayout {

    ...    

    public DemoView(@Inject ModelAccessor<Model> accessor) {
        
        ...

        add(HorizontalLayoutBuilder.create().
                add(TextFieldBuilder.create().
                        setLabel("Sensitive Field").
                        bindAndConfigure(accessor, SENSITIVE_PROPERTY).
                        setAudit(Binding.AccessMode.READ_WRITE, Expression.orOf(DemoUser.RIGHT_SUPER_SENSITIVE_DATA, DemoUser.RIGHT_SENSITIVE_DATA)).
                        bind()).
                add(TextFieldBuilder.create().
                        setLabel("Super Sensitive Field").
                        bindAndConfigure(accessor, SUPER_SENSITIVE_PROPERTY).
                        setAudit(Binding.AccessMode.READ_WRITE, DemoUser.RIGHT_SUPER_SENSITIVE_DATA).
                        setAudit(Binding.AccessMode.READ_ONLY, DemoUser.RIGHT_SENSITIVE_DATA).
                        bind()).
                build());
    }
}
```

The different restriction types we use here are called **_AccessMode_**, where:
- **_AccessMode_.READ_WRITE** will cause the **_TextField_** to be editable
- **_AccessMode_.READ_ONLY** will cause the **_TextField_** to be displaying the property only
- **_AccessMode_.MASKED** will cause the **_TextField_** to be displaying an empty value only
- **_AccessMode_.HIDDEN** will cause the **_TextField_** to become invisible

In case of the data with normal sensitivity, we can allow **_AccessMode_.READ_WRITE** to any **_User_** that has the right for it or super sensitivity; we can define any complex right conjunctions using the **_Expression_** syntax.

In the other case of super sensitive data, we simply create individual audits for the different modes.