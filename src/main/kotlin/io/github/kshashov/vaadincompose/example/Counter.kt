package io.github.kshashov.vaadincompose.example

import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.router.*
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

    class MainWidget : StatefulWidget() {
        override fun createState() = MainState()
    }

    class MainState : StatefulWidget.WidgetState<MainWidget>() {
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
}
