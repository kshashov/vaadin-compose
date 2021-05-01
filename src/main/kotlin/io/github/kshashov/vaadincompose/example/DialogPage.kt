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
import io.github.kshashov.vaadincompose.widget.components.Dialog
import io.github.kshashov.vaadincompose.widget.components.HasDialog

@CssImport("./styles/styles.css")
@Route("dialog")
class DialogPage : Div(), ComposablePage {

    override fun build(context: BuildContext): Widget {
        return MainWidget()
    }

    class MainWidget : StatefulWidget<MainState>() {
        override fun createState() = MainState()
    }

    class MainState : StatefulWidget.WidgetState() {
        private var show = false;

        override fun build(context: BuildContext): Widget {
            return HasDialog(
                child = Container(
                    direction = FlexLayout.FlexDirection.COLUMN,
                    classes = listOf("main-widget"),
                    childs = listOf(
                        Button("open", { setState { show = true } })
                    )
                ),
                dialog = Dialog(
                    Button("close", { setState { show = false } }),
                    show = show
                )
            )
        }

    }
}
