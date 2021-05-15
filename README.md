# vaadin-compose

Vaadin Compose Spring Boot Starter

```kotlin
@CssImport("./styles/styles.css")
@Route("counter")
class Page : ComposablePage() {

    override fun build(context: BuildContext): Widget {
        return MainWidget()
    }
}

class MainWidget : StatefulWidget<MainState>() {
    override fun createState() = MainState()
}

class MainState : StatefulWidget.WidgetState() {
    private var counter: Int = 0

    override fun build(context: BuildContext): Widget {
        return Container(
            direction = FlexLayout.FlexDirection.COLUMN,
            classes = listOf("main-widget"),
            childs = listOf(
                Label("Counter: $counter") {
                    style {
                        set("font-weight", "bold")
                    }
                },
                Button("+1", {
                    setState { counter++ }
                })
            )
        )
    }

}
```

## Architecture

### First rendering

### Further updating

#### StatelessWidget

#### StatefulWidget

#### RenderWidget

## Page

## Services interaction

The most convenient method here is to expose your data as a RxJava streams in MVVM style (see Bloc pattern).

### Provider

Firstly, we need to propagate our services to our widgets throught the entire widgets hierarchy. We can do it
with `Provider` widget:

```kotlin
// Root widget
override fun build(context: BuildContext): Widget {
    return Provider(child = MainWidget(), service = MyBloc())
}

// Nested Target Widget
override fun build(context: BuildContext): Widget {
    val bloc: MyBloc = Provider.of(DebugToolsBloc::class.java, context)!!
    ...
}
```

### StreamConsumer

By default, it is supposed to use our own StateFul Widgets to reload state according our business logic. However, it is
the common case when we should refresh the widget's subtree when new data has come from stream. There's
a `StreamConsumer` widget for such cases. It automatically subscribes and unsubscribes on the target stream and reloads
nested sub-tree when a new value has been received from the stream.

```kotlin
override fun build(context: BuildContext): Widget {
    return StreamConsumer(
        initial = Snapshot.withData(""),
        stream = bloc.texts,
        builder = { text -> Label(text.requireData()) })
}
```

## VCompose DSL

I found it quite ugly to list child widgets by specifying `childs = listOf(..)` method parameters so I tried to create
custom DSL:

```kotlin
    override fun build(context: BuildContext): Widget {
    return ui {
        container {
            direction = FlexLayout.FlexDirection.COLUMN
            classes = listOf("main-widget")

            label("Counter: $counter") {
                style { set("font-weight", "bold") }
            }
            button("+1") {
                onClick = {
                    setState { counter++ }
                }
            }
        }
    }
}

```

The original idea was to make something similar to the [VoK solution](https://www.vaadinonkotlin.eu//dsl_explained/).
But after using this dsl, I realized that the creation of each new widget is accompanied by a pain in the ass while
maintaining dsl for this widget. This fundamentally contradicts the original idea of simplifying the interface
declaration by spliting it into many simple widgets (divide and conquer), so in the end I decided to get rid of such dsl
and use just a couple of extension methods for declarative work with Vaadin API.

For now each Render Widget has Vaadin compoennt consumer as the latest constructor argument. In additiona to some
extention methods it allows me to easily add some final twicks to resulted Vaadin component:

```kotlin
class Label(
    ...
    postProcess: (com.vaadin.flow.component.html.Label.() -> Unit)? = null
) ...

fun (@VComposeDsl Component).style(styleProcess: (@VComposeDsl Style).() -> Unit) {
    this.element.style.styleProcess()
}

fun (@VComposeDsl Component).element(elementProcess: (@VComposeDsl Element).() -> Unit) {
    this.element.elementProcess()
}

label("Counter: $counter") {
    style { set("font-weight", "bold") }
}
```
