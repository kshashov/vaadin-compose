package io.github.kshashov.vaadincompose.example

import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeLeaveEvent
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.internal.BeforeEnterHandler
import com.vaadin.flow.router.internal.BeforeLeaveHandler
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
class DialogPage : Div(), ComposablePage, BeforeEnterHandler, BeforeLeaveHandler {

    override fun beforeEnter(event: BeforeEnterEvent?) {
        TODO("Not yet implemented")
    }

    override fun beforeLeave(event: BeforeLeaveEvent?) {
        TODO("Not yet implemented")
    }

    override fun build(context: BuildContext): Widget {
        return MainWidget()
    }

    class MainWidget : StatefulWidget() {
        override fun createState() = MainState()
    }

    class MainState : StatefulWidget.WidgetState<MainWidget>() {
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
