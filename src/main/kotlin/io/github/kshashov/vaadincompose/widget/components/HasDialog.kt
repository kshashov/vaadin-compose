package io.github.kshashov.vaadincompose.widget.components

import io.github.kshashov.vaadincompose.widget.*

class HasDialog(
    val child: Widget,
    val dialog: Dialog,
    key: String? = null
) : RenderWidget<RenderElement.EmptyWidget>(key) {

    override fun createElement(): Element<HasDialog> {
        return EagerDialogRenderElement(this)
    }

    class EagerDialogRenderElement(widget: HasDialog) :
        EagerChildRenderElement<HasDialog, RenderElement.EmptyWidget>(widget) {

        override fun updateContextChilds(list: Collection<Widget>) {
            super.updateContextChilds(list)
            proxyRenderedComponent()
        }

        override fun createComponent() = EMPTY_WIDGET

        override fun refreshComponent() {
            // Do nothing
        }

        override fun getChilds() = listOf(this.widget.child, this.widget.dialog)

        private fun proxyRenderedComponent() {
            renderedComponent = context.childs[0].element.renderedComponent
        }
    }
}
