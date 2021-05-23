package io.github.kshashov.vaadincompose.example

import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.router.Route
import io.github.kshashov.vaadincompose.BaseComposablePage
import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.widget.StatefulWidget
import io.github.kshashov.vaadincompose.widget.StatelessWidget
import io.github.kshashov.vaadincompose.widget.Widget
import io.github.kshashov.vaadincompose.widget.components.*

@Description("Examples of conditional rendering with manual <code>if</code> statement and <code>Conditional</code> widget")
@Route("conditional", layout = Root::class)
class ConditionalPage : BaseComposablePage() {
    override fun isDebug() = true

    override fun build(context: BuildContext): Widget {
        return MainWidget()
    }

    class MainWidget(key: String? = null) : StatefulWidget(key) {
        override fun createState() = MainState()
    }

    class MainState : StatefulWidget.WidgetState<MainWidget>() {
        private var switcher1: Boolean = false
        private var switcher2: Boolean = false
        private var switcher3: Boolean = false
        private var switcher4: Boolean = false

        override fun build(context: BuildContext): Widget {
            return Container(
                direction = FlexLayout.FlexDirection.COLUMN,
                childs = mutableListOf(
                    if (switcher1) Wrapper(Label("Stateless True")) else Wrapper(Text("Stateless False")),
                    Button("Switch with if", {
                        setState { switcher1 = !switcher1 }
                    }),
                    if (switcher2) Wrapper2(
                        Label("Stateful True with key = 'true'"),
                        key = "true"
                    ) else Wrapper2(Text("Stateful False with key = 'false'"), key = "false"),
                    Button("Switch with if", {
                        setState { switcher2 = !switcher2 }
                    }),
                    Conditional(
                        switcher3,
                        primaryBuilder = { Wrapper(Label("Stateless True")) },
                        secondaryBuilder = { Wrapper(Text("Stateless False")) }
                    ),
                    Button("Switch with Conditional", {
                        setState { switcher3 = !switcher3 }
                    }),
                    Conditional(
                        switcher4,
                        primaryBuilder = { Wrapper2(Label("Stateful True")) },
                        secondaryBuilder = { Wrapper2(Text("Stateful False")) }
                    ),
                    Button("Switch with Conditional", {
                        setState { switcher4 = !switcher4 }
                    })
                )
            )

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
}
