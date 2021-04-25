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
    override fun createState() =  MainState()
}

class MainState : StatefulWidget.WidgetState() {
    private var counter: Int = 0

    override fun build(context: BuildContext): Widget {
        return Container(
                direction = FlexLayout.FlexDirection.COLUMN,
                classes = listOf("main-widget"),
                components = listOf(
                        Label("Counter: $counter"),
                        Button("+1", {
                            setState { counter++ }
                        })))
    }

}
```
