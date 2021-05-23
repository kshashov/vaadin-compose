package io.github.kshashov.vaadincompose.example

import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.router.Route
import io.github.kshashov.vaadincompose.BaseComposablePage
import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.widget.StatefulWidget
import io.github.kshashov.vaadincompose.widget.Widget
import io.github.kshashov.vaadincompose.widget.components.Button
import io.github.kshashov.vaadincompose.widget.components.Container
import io.github.kshashov.vaadincompose.widget.components.Dialog
import io.github.kshashov.vaadincompose.widget.components.HasDialog

@Description("Example of <code>Dialog</code> widget usage. The dialog can be opened by setting <code>show = true</code> parameter")
@Route("dialog", layout = Root::class)
class DialogPage : BaseComposablePage() {

    override fun isDebug() = true

    override fun build(context: BuildContext): Widget {
        return MainWidget()
    }

    class MainWidget : StatefulWidget() {
        override fun createState() = MainState()
    }

    class MainState : StatefulWidget.WidgetState<MainWidget>() {
        private var show = false

        override fun build(context: BuildContext): Widget {
            return HasDialog(
                child = Container(
                    direction = FlexLayout.FlexDirection.COLUMN,
                    childs = listOf(
                        Button("open", onClick = { setState { show = true } })
                    )
                ),
                dialog = Dialog(
                    child = Button("close", onClick = { setState { show = false } }),
                    show = show
                )
            )
        }
    }
}
