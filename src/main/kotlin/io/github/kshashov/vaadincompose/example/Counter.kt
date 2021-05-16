package io.github.kshashov.vaadincompose.example

import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.router.Route
import io.github.kshashov.vaadincompose.BaseComposablePage
import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.ComposablePage
import io.github.kshashov.vaadincompose.style
import io.github.kshashov.vaadincompose.widget.StatefulWidget
import io.github.kshashov.vaadincompose.widget.Widget
import io.github.kshashov.vaadincompose.widget.components.Button
import io.github.kshashov.vaadincompose.widget.components.Container
import io.github.kshashov.vaadincompose.widget.components.Label

@CssImport("./styles/styles.css")
@Route("counter")
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
