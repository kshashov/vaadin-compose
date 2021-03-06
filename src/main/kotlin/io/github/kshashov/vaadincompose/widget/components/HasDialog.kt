package io.github.kshashov.vaadincompose.widget.components

import io.github.kshashov.vaadincompose.widget.EagerChildsElement
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.Widget

class HasDialog(
    private val child: Widget,
    private val dialog: Dialog,
    key: String? = null
) : Widget(key) {

    override fun createElement(): Element<HasDialog> {
        return HasDialogElement(this)
    }

    internal class HasDialogElement(widget: HasDialog) : EagerChildsElement<HasDialog>(widget) {
        override fun getChilds() = listOf(this.widget.child, this.widget.dialog)

        override fun proxiedComponentIndex(): Int {
            return 0
        }
    }
}
