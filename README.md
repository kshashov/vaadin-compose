# Vaadin Compose

[![JitPack](https://jitpack.io/v/kshashov/vaadin-compose.svg)](https://jitpack.io/#kshashov/vaadin-compose)
[![Build Status](https://travis-ci.com/kshashov/vaadin-compose.svg?branch=main)](https://travis-ci.com/kshashov/vaadin-compose)
[![codecov](https://codecov.io/gh/kshashov/vaadin-compose/branch/main/graph/badge.svg?token=3N9RLWMRQT)](https://codecov.io/gh/kshashov/vaadin-compose)

![Counter](/img/vaadin-compose-counter.gif "Counter")

```kotlin
@Route("counter")
@CssImport("./styles/styles.css")
class Counter : BaseComposablePage(), ComposablePage {

   override fun build(context: BuildContext) = MainWidget()

   class MainWidget : StatefulWidget() {
      override fun createState() = MainState()

      class MainState : StatefulWidget.WidgetState<MainWidget>() {
         private var counter: Int = 0

         override fun build(context: BuildContext): Widget {
            return Container(
               direction = FlexLayout.FlexDirection.COLUMN,
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
   }
}
```

## Install

### Maven

```xml

<repositories>
   <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
   </repository>
</repositories>
```

```xml

<dependencies>
   <dependency>
      <groupId>com.github.kshashov</groupId>
      <artifactId>vaadin-compose</artifactId>
      <version>0.0.5</version>
   </dependency>
</dependencies>
```

### Gradle

```groovy
allprojects {
   repositories {
      ...
      maven { url 'https://jitpack.io' }
   }
}

dependencies {
   implementation 'com.github.kshashov:vaadin-compose:0.0.5'
}
```

## Architecture Design

The basic design concepts were borrowed from [Flutter](https://flutter.dev/docs/resources/architectural-overview)
framework.

In general, the programmers just need to declare UI using widgets structure. Widgets are the building blocks of a app’s
user interface, and each widget is an immutable declaration of part of the user interface.

### Rendering

As with Flutter, widgets here are just configurations, so they don't work with render (Vaadin) side. Just the opposite
to widgets there is a Elements hierarchy that is intended to create and update Vaadin Components according to widgets
hierarchy.

Shortly, the first rendering is performed in the following way:

1. The `build(BuildContext)` widget's methods are invoked to build widgets structure.
   * For each widgets the BuildContext nodes are created. In result, the BuildContext tree has exactly the same
     structure as widgets tree has. Each BuildContext has Element instance that has been created by `createElement`
     widget method so BuildContext tree will be mentioned as Element tree in the following notes
2. Each Element creates and configures Vaadin Component instances. Some of the Elements do nothing here and just
   propagate Component from nested Elements.
3. After the entire Element tree is rendered the rendered Component from root Element can be added to the target page.

#### RenderWidget

Some of the widgets are inherited from `RenderWidget`. it means that their Elements are intended to generate Vaadin
Components according to widget's properties: `Button`, `Container`, `DataTable`, `TreeDataTable`, `Details`, `Dialog`
, `Label`, `SplitLayout`, `Text`.

The other widgets are generally interited from `StatelessWidget` and `StatefulWidget` classes. It means that their
Elements just propagate already rendered Vaadin Component from nested Elements: `Conditional`, `Details`,
`HasDialog`, `ListView`, `Provider`, `StreamConsumer`, `Table`.

The full widgets list can be found in the `io.github.kshashov.vaadincompose.widget.components` [package]
(src/main/kotlin/io/github/kshashov/vaadincompose/widget/components).

### Further updating

This is what a separate Element tree is needed for.

The framework introduces two major classes of widget: stateful and stateless widgets. Many widgets have no mutable
state: they don’t have any properties that are changed over time. These widgets subclass `StatelessWidget`. However, if
the widget properties need to change based on user interaction or other factors, the `StatefulWidget`
should be used as a base class.

#### StatefulWidget

For example, if a widget has a counter that increments whenever the user taps a button, then the value of the counter is
the state for that widget. When that value changes, the widget needs to be rebuilt to update its part of the UI. These
widgets subclass `StatefulWidget`, and (because the widget itself is immutable) they store mutable state in a separate
class that subclasses `WidgetState`. Stateful Widgets don't have a build method; instead, their user interface is built
through their `WidgetState` object.

```kotlin
class MainWidget : StatefulWidget<MainState>() {
   override fun createState() = MainState()
}

class MainState : StatefulWidget.WidgetState() {
   private var counter: Int = 0

   override fun build(context: BuildContext): Widget {
      return Container(
         childs = listOf(
            Label("Counter: $counter"),
            Button("+1", { setState { counter++ } })
         )
      )
   }
}
```

Whenever we mutate a `WidgetState` object (for example, by incrementing the counter), we **must call** `setState()` to
signal the framework to update the entire Element sub-tree.

#### How it works

Actially, the `WidgetState` just proxies updating invocation to `StatefulElement` that rebuilds sub-tree in the
following way:

1. Recreates Widget tree by `build(BuildContext)` recursive invocation.
2. Attach new widgets to already created Elements if possible. The old Element is reused only if the previous Widget Key
   is equals to the new one. The new Element will be created If there is no Elements for the current Widget Key.
    * By default, the Widget Key is generated by pattern `"${widget.javaClass.name}${index}"`. Some of the Elements may
      override this behaviour to add some tweaks
    * The obsolete Element is removed from cache if there is no custom logic on the root Element side. For example,
      the `ConditionalElement` saves the latest Element child for each condition branch to get rid of Element recreation
      on each condition change
    * In case of Element re-attaching, the Vaadin Component is just updated and isn't recreated.
3. After the Element sub-tree is refreshed, the rendered Component from sub-tree root is replaced by the new one.

![Elements tree](/img/compose-elements-tree.png "Elements Tree")

**Important!**
Since the Widget key is not required for `StatelessWidget`, it may be to easy to forget about it in some important
moments. The common rule here is to specify Widget Key if using multiple Stateful Widget instances of the same class as
a childs and their order could be changed during refresh. Without specified key it will be too hard for framework to
determine what StatefulElement should be used for new rebuilded widget because the default key doesn't store any unique
identifiers.

Here we set a key for nested `TodoItemWidget` widgets because their order could be changed later:

```kotlin
return StreamConsumer(
   initial = Snapshot.withData(mutableListOf()),
   stream = bloc.todos,
   builder = { items ->
      ListView(
         height = "100%",
         direction = FlexLayout.FlexDirection.COLUMN,
         items = items.requireData(),
         render = { TodoItemWidget(item = it, key = it.id.toString()) }
      )
   })
```

## Page

To start using Vaadin Compose Widgets we need to implement `ComposablePage` interface in our Page.

* Invoke `init()` method when the Page is ready to add a rendered Vaadin Component
* Invoke `dispose()` method to trigger the same method invocations in the nested Elements and Widget States before the
  Page is closed

```kotlin
@CssImport("./styles/styles.css")
@Route("counter")
class Counter : Div(), ComposablePage, BeforeEnterObserver, BeforeLeaveObserver {

   private lateinit var context: BuildContext

   override fun build(context: BuildContext): Widget {
      return MainWidget()
   }

   override fun beforeEnter(event: BeforeEnterEvent?) {
      context = init()
   }

   override fun beforeLeave(event: BeforeLeaveEvent?) {
      dispose(context)
   }
}
```

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

By default, it is supposed to use our own Stateful Widgets to reload state according to our business logic. However, it
is the common case when we should refresh the widget's subtree when new data has come from stream, so there's
a `StreamConsumer` widget for such cases. It automatically subscribes and unsubscribes on the target stream and reloads
nested sub-tree when a new value has been received from the stream.

```kotlin
return StreamConsumer(
   initial = Snapshot.withData(""),
   stream = bloc.texts,
   builder = { text -> Label(text.requireData()) }
)
```

## VCompose DSL

I found it quite ugly to list child widgets by specifying `childs = listOf(..)` method parameters so I tried to create a
new custom DSL:

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

For now each Render Widget has Vaadin component consumer as the latest constructor argument. In additional to some
extention methods it allows me to easily add some final twicks to the resulted Vaadin component:

```kotlin
// Custom VCompose DSL
fun (@VComposeDsl Component).style(styleProcess: (@VComposeDsl Style).() -> Unit) {
   this.element.style.styleProcess()
}

fun (@VComposeDsl Component).element(elementProcess: (@VComposeDsl Element).() -> Unit) {
   this.element.elementProcess()
}

// Add support to Label widget
class Label(
   ...
   postProcess: (com.vaadin.flow.component.html.Label.() -> Unit)? = null
) ...


// Usage
label("Counter: $counter") {
   style { set("font-weight", "bold") }
}
```

## Debug Panel Widget

Sometimes it isn't so easy to debug Element's hierarchy, so there's a `ComposablePage#isDebug` method. When it returns
true, the debug panel is added to the page. It includes Widget tree with detailed information of each Node - Widget,
Element, Vaadin Component and Cache:

![Debug Panel](/img/debug-panel.png "Debug Panel")
