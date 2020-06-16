# Chapter 2.e: Consuming Metrics

Cotton implements the [TrailMetrics support for Vaadin Flow](https://github.com/MantledIllusion/trail-metrics/tree/master/trail-metrics-support-vaadin-flow) that enables dispatching session based metrics from anywhere in the application and even dispatches a set of general metrics itself.

If desired, all dispatched metrics can be consumed and then be used for any purpose desired.

## Committing Metrics

In every **_Cotton_** session the **_MetricsTrailSupport_** can be used to retrieve the current **_MetricsTrail_**, so metrics can be committed from anywhere; for example, our **_DemoView_**:

````java
@Route("demo")
public class DemoView extends HorizontalLayout {

    private static final Consumer<ClickEvent> METRICS_DISPATCHER = event ->
            MetricsTrailSupport.commit(new Metric("clickmetric", MetricType.ALERT));

    public DemoView() {
        Button b = new Button("Create Click Metric");
        b.addClickListener(METRICS_DISPATCHER::accept);
        add(b);

        b = new Button("Cause Error");
        b.addClickListener(event -> {
            throw new RuntimeException("Boo-Hoo!");
        });
        add(b);

        b = new Button("Nav Error");
        b.addClickListener(event -> WebEnv.navigateTo(FailureView.class));
        add(b);
    }
}
````

Every click on the first button now generates a new simple **ALERT** **_Metric_** with the identifier "_clickmetric_". It will be delivered to all consumers asynchronously by the **_MetricsTrail_**.

Every click on the second causes an exception that will be shown in an error dialog, together with the trail ID.

Every click on the third button will cause a navigation to a view that immediately fails, resulting in an error page to be shown. The set up for **_FailingView_** is trivial:

```java
@Route("failure")
public class FailureView extends Div {

    public FailureView() {
        throw new RuntimeException("Error!");
    }
}
```

## Registering a MetricsConsumer

The **_TrailMetrics_** API supplies the interface **_MetricsConsumer_**, whose implementations are able to receive metrics from every trail, while a trail in the **_Vaadin_** support equals a session.

First, we will implement a cache for our metrics, so we can access them from anywhere:

````java
public class DemoMetricsCache {

    public static final String QUALIFIER_CACHE = "_demoMetricsCache";

    private final Map<UUID, List<Metric>> trailCache = new ConcurrentHashMap<>();

    public void add(UUID trailId, Metric metric) {
        if (CottonMetrics.SESSION_END.getMetricId().equals(metric.getIdentifier())) {
            this.trailCache.remove(trailId);
        } else {
            this.trailCache.computeIfAbsent(trailId, id -> Collections.synchronizedList(new ArrayList<>())).add(metric);
        }
    }

    public List<Metric> getSessionMetrics() {
        if (!MetricsTrailSupport.has() || !this.trailCache.containsKey(MetricsTrailSupport.id())) {
            return Collections.emptyList();
        } else {
            return this.trailCache.get(MetricsTrailSupport.id());
        }
    }
}
````

The cache is able to receive a metric for a trail and return all metrics of the current session's trail. Note that we clear the all metrics of a session once the session ends.

Now that we have a cache to put metrics in, we create a consumer to do that. Consumers can be registered using the environment **_Blueprint_**:

````java
@Inject
private DemoMetricsCache cache;

@Define
public SingletonAllocation defineMetricsConsumer() {
    MetricsConsumer consumer = (consumerId, trailId, metric) -> this.cache.add(trailId, metric);
    return CottonEnvironment.forMetricsConsumer("demoConsumer", consumer, null, null);
}

@Define
public SingletonAllocation defineCache() {
    return SingletonAllocation.of(DemoMetricsCache.QUALIFIER_CACHE, this.cache);
}
````

A simple lambda for our consumer is enough. Every metric from every session coming in will be put in the cache now. Keep in mind that the consumer will be called asynchronously using threads parallel to the one serving the session, so we have to use concurrent map and list instances.

Since we also defined our cache as a singleton, we can access it from anywhere in our application; for example our **_MetricsView_**, where we can display them all:

````java
@Route("metrics")
public class MetricsView extends Div {

    private static final Function<Metric, String> OPERATOR_RETRIEVER = metric ->
            StringUtils.join(metric.getAttributes(), ", ");

    public MetricsView(@Inject @Qualifier(DemoMetricsCache.QUALIFIER_CACHE) DemoMetricsCache cache) {
        Grid<Metric> grid = new Grid<>();
        grid.addColumn(Metric::getIdentifier).setHeader("Identifier").setFlexGrow(0).setWidth("200px");
        grid.addColumn(Metric::getType).setHeader("Type").setFlexGrow(0);
        grid.addColumn(Metric::getTimestamp).setHeader("Timestamp").setFlexGrow(0).setWidth("300px");
        grid.addColumn(OPERATOR_RETRIEVER::apply).setHeader("Operator").setFlexGrow(1);
        add(grid);

        grid.setItems(cache.getSessionMetrics());
    }
}
````

When accessing the view the grid will display any click metric from the **_DemoView_**, but also Metrics from the **_CottonMetrics_** enum of **_Cotton_** itself, like _cotton.system.injection_.

These enums contain metrics that record common events like visits, navigations or even errors.