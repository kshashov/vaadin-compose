package io.github.kshashov.vaadincompose.example

import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.router.Route
import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.ComposablePage
import io.github.kshashov.vaadincompose.widget.StatefulWidget
import io.github.kshashov.vaadincompose.widget.Widget
import io.github.kshashov.vaadincompose.widget.components.Button
import io.github.kshashov.vaadincompose.widget.components.Container
import io.github.kshashov.vaadincompose.widget.components.Label

@CssImport("./styles/styles.css")
@Route("counter")
class Counter : Div(), ComposablePage {

    override fun build(context: BuildContext): Widget {
        return MainWidget()
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
                    components = listOf(
                            Label("Counter: $counter"),
                            Button("+1", {
                                setState { counter++ }
                            })))
        }

    }
}
