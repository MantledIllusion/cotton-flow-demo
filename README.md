# Chapter 3.e: Building a responsive UI

Building responsive web apps is hard in Vaadin, as the code being on the server is setting up layout and components, but you might want your application to completely change layout when being delivered to different platforms or screen sizes.

Cotton aids for this problem, by enabling developers to provide multiple implementations for the same route and just specifying which implementation able to handle which scenario.

## Declaring a responsive View

First, we set up an abstract view class that we can reuse for the responsive variants we will implement.

We will just declare it to be a **_Div_** that tries to show a lot of content:

```java
public abstract class AbstractResponsiveView extends Div {

    public AbstractResponsiveView() {
        add(wrapInLayout(IntStream.range(0, 100).
                mapToObj(i -> new Label("This is the #"+i+" content")).
                collect(Collectors.toList()).
                toArray(new Component[0])));
    }

    abstract Component wrapInLayout(Component[] components);
}
```

Now we create two variants of that abstract view: first, a default one for landscape format devices (screens wider than tall):

```java
@Route("demo")
@Responsive(@Responsive.Alternative(value = PortraitView.class, mode = Responsive.Alternative.ScreenMode.RATIO,
        fromX = 1, fromY = 1, toX = 1, toY = Integer.MAX_VALUE))
public class LandscapeView extends AbstractResponsiveView {

    @Override
    Component wrapInLayout(Component[] components) {
        return HorizontalLayoutBuilder.create().setWidthUndefined().setHeightFull().add(components).build();
    }
}
```

We routed the /demo URL to the view, but we also defined an alternative for portrait format devices (screens taller than wide).

In the _@Alternative_'s settings, we defined we want to match for **_ScreenMode_.RATIO** and then defined the from/to values in a way that our portrait view will be used from screens with 1:1 ratios until the most vertical format possible.

Now, we can implement the portrait variant:

```java
public class PortraitView extends AbstractResponsiveView {

    @Override
    Component wrapInLayout(Component[] components) {
        return VerticalLayoutBuilder.create().setWidthFull().setHeightUndefined().add(components).build();
    }
}
```

Note that we did not define a _@Route_ on the **_PortraitView_**, since referring to the view from the **_LandScape_** one is enough.

With this configuration, the correct view for each screen size is injected automatically when visiting the route. Additionally, the view will be switched automatically every time the client browser's screen format changes.

## Denying automatic View switches

Since views are injected and also destroyed every time a responsive view switch is performed, there is the risk of loosing user progress on the view that is being replaced.

To prohibit this, a **_BeforeResponsiveRefreshListener_** can be used that is registered at the **_CottonUI_**. In our case, we will use a pop-up dialog that asks the user if switching is preferred:

```java
@Presented(ResponsiveDialogPresenter.class)
public class ResponsiveDialogView extends AbstractView {

    static final String CID_YES = "yes";
    static final String CID_NO = "no";

    @Override
    protected Component buildUI(TemporalActiveComponentRegistry reg) throws Exception {
        return VerticalLayoutBuilder.create().
                add(LabelBuilder.create().setValue("Do you want to switch?").build()).
                add(HorizontalLayoutBuilder.create().
                        add(ButtonBuilder.create().setText("Yes").setRegistration(reg, CID_YES).build()).
                        add(ButtonBuilder.create().setText("No").setRegistration(reg, CID_NO).build()).
                        build()).
                build();
    }
}
```

```java
public class ResponsiveDialogPresenter extends AbstractPresenter<ResponsiveDialogView> implements CottonUI.BeforeResponsiveRefreshListener {

    private Dialog dialog;

    @PostConstruct
    private void init() {
        CottonUI.current().addBeforeResponsiveRefreshListener(this);
        this.dialog = new Dialog(getView());
        this.dialog.close();
    }

    @Override
    public void beforeRefresh(BeforeResponsiveRefreshEvent event) {
        if (!event.isForced()) {
            event.decline();
            if (!this.dialog.isOpened()) {
                this.dialog.open();
            }
        }
    }

    @Listen(ResponsiveDialogView.CID_YES)
    private void handleYes(ClickEvent e) {
        this.dialog.close();
        WebEnv.adaptResponsive();
    }

    @Listen(ResponsiveDialogView.CID_NO)
    private void handleNo(ClickEvent e) {
        this.dialog.close();
    }
}
```

To enable the dialog to be active, we simply have to inject it into both the **_LandscapeView_** and **_PortraitView_**:

```java
@Inject
private ResponsiveDialogView responsiveDialogView;
```