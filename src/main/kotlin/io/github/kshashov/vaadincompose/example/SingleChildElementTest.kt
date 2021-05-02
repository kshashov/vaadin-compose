package io.github.kshashov.vaadincompose.example

import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.router.Route
import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.ComposablePage
import io.github.kshashov.vaadincompose.widget.StatefulWidget
import io.github.kshashov.vaadincompose.widget.StatelessWidget
import io.github.kshashov.vaadincompose.widget.Widget
import io.github.kshashov.vaadincompose.widget.components.*

@CssImport("./styles/styles.css")
@Route("singlechild")
class SingleChildElementTest : Div(), ComposablePage {

    override fun build(context: BuildContext): Widget {
        return MainWidget()
    }

    class MainWidget(key: String? = null) : StatefulWidget(key) {
        override fun createState() = MainState()
    }

    class MainState : StatefulWidget.WidgetState<MainWidget>() {
        private var switcher1: Boolean = false
        private var switcher2: Boolean = false

        override fun build(context: BuildContext): Widget {
            return Container(
                direction = FlexLayout.FlexDirection.COLUMN,
                classes = listOf("main-widget"),
                childs = listOf(
                    if (switcher1) Wrapper(Label("True")) else Wrapper(Text("False")),
                    Button("Switch stateless", {
                            setState { switcher1 = !switcher1 }
                        }),
                        Conditional(
                            switcher2,
                            first = Wrapper2(Label("True")),
                            second = Wrapper2(Text("False"))
                        ),
                        Button("Switch stateful", {
                            setState { switcher2 = !switcher2 }
                        })
                    ))
        }
    }

    class Wrapper(private val child: Widget, key: String? = null) : StatelessWidget(key) {
        override fun build(context: BuildContext): Widget {
            return child
        }
    }

    class Wrapper2(val child: Widget, key: String? = null) : StatefulWidget(key) {

        override fun createState() = Wrapper2State(child)

        class Wrapper2State(private val child: Widget) : WidgetState<Wrapper2>() {
            override fun build(context: BuildContext): Widget {
                return child
            }

            override fun detach() {
                super.detach()
            }

            override fun dispose() {
                super.dispose()
            }
        }
    }
}
