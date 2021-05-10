package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.dom.Style
import io.github.kshashov.vaadincompose.widget.EagerChildsRenderElement
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.RenderWidget
import io.github.kshashov.vaadincompose.widget.Widget
import com.vaadin.flow.component.dialog.Dialog as VaadinDialog

class Dialog(
    val child: Widget,
    val show: Boolean,
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null,
    styleProcess: ((style: Style) -> Unit)? = null,
    postProcess: ((VaadinDialog) -> Unit)? = null,
) : RenderWidget<VaadinDialog>(key, height, width, id, classes, alignItems, justifyContent, styleProcess, postProcess) {

    override fun createElement(): Element<Dialog> {
        return DialogRenderElement(this)
    }

    class DialogRenderElement(widget: Dialog) : EagerChildsRenderElement<Dialog, VaadinDialog>(widget) {

        override fun createComponent(): VaadinDialog {
            return VaadinDialog()
        }

        override fun refreshComponent() {
            super.refreshComponent()
            component.isOpened = widget.show
        }

        override fun getChilds() = listOf(this.widget.child)
    }
}
