# Chapter 4.c: Right Dependent Data Binding

As business applications grow, they often become used by more than one type of user. As soon as there is more than one user group, the need will arise to restrict data access depending on the different groups, often upto field precision.

Cotton allows specifying the access mode per-field in the data binding, effectively preventing data from being modified (or even seen) by users without sufficient rights.

## Setting up a Model

For this lesson we do not need a complex model, a simple one with one **_String_** field is enough:

```java
public class Model {

    private String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
```

## Setting up a User

Since we are depending on access rights here, we require a **_User_** that can be checked for rights.

We only require it to have 1 right type that has editing capabilities, or none at all:

```java
public class DemoUser implements User {

    static final String RIGHT_EDITOR = "_editor";

    static final User USER_EDITOR = new DemoUser(RIGHT_EDITOR);
    static final User USER_NONE = new DemoUser();

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

For our view, we set up 3 things to get going:
- Create a **_ModelProperty_** to bind on instances of our **_Model_** class
- Set up some buttons to log in/out our different types of users
- Inject a **_ModelAccessor_**, setting a dummy model

Then, we can create the restricted binging between our ModelProperty and a TextField. This can be done using the _**ModelAccessor**.bindAndConfigure()_ method, that allows us to configure the **_Binding_** we create; this method's functionality is also embedded into the builders, so it can be used at the end of building a component:

```java
@Route("demo")
public class DemoView extends VerticalLayout {

    private static final ModelProperty<Model, String> FIELD = ModelProperty.fromObject(Model::getField, Model::setField);

    public DemoView(@Inject ModelAccessor<Model> accessor) {
        add(new HorizontalLayoutBuilder().
                add(new ButtonBuilder().
                        setValue("Login Editor").
                        addClickListener(e -> WebEnv.logIn(DemoUser.USER_EDITOR)).
                        build()).
                add(new ButtonBuilder().
                        setValue("Login Guest").
                        addClickListener(e -> WebEnv.logIn(DemoUser.USER_NONE)).
                        build()).
                add(new ButtonBuilder().
                        setValue("Logout").
                        addClickListener(e -> WebEnv.logOut()).
                        build()).
                build());

        add(new TextFieldBuilder().
                bindAndConfigure(accessor, FIELD).
                withRestriction(User.UserRightBindingAuditor.readWrite(DemoUser.RIGHT_EDITOR)).
                withRestriction(User.UserRightBindingAuditor.readOnly()).
                bind());

        Model model = new Model();
        model.setField("Some sensitive value");
        accessor.setModel(model);
    }
}
```

When no restriction is created at all, **Cotton** will automatically assume a binding to be non-threatening, so it will allow full read/write access to it.

As soon as at least one restriction is created, this logic is turned around: everything is forbidden if there is not a restriction explicitly allowing it.

In our example we added 2 restrictions:
- A read/write restriction, granted only to users that have the editor right
- A read-only restriction, granted to users that have no specific right (which means they just have to be logged in)

As a result, if no user is logged in at all, the binding's access to the property is restricted completely.

The different restriction types are called **_AccessMode_**, where:
- **_AccessMode_.READ_WRITE** will cause the **_TextField_** to be editable
- **_AccessMode_.READ_ONLY** will cause the **_TextField_** to be displaying only
- **_AccessMode_.PROHIBIT** will cause the **_TextField_** to become invisible