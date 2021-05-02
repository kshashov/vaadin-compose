package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.html.Div
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.HasChildRenderElement
import io.github.kshashov.vaadincompose.widget.RenderWidget
import io.github.kshashov.vaadincompose.widget.Widget

class HasDialog(
    val child: Widget,
    val dialog: Dialog,
    key: String? = null
) : RenderWidget<Div>(key) {

    override fun createElement(): Element<HasDialog> {
        return HasDialogRenderElement(this)
    }

    class HasDialogRenderElement(widget: HasDialog) : HasChildRenderElement<HasDialog, Div>(widget) {

        override fun updateContextChilds(list: Collection<Widget>) {
            super.updateContextChilds(list)
            proxyRenderedComponent()
        }

        override fun onAfterRender() {
            super.onAfterRender()
            proxyRenderedComponent()
        }

        override fun createComponent(): Div {
            // Component will not be used since we will proxy nested component later
            return Div()
        }

        override fun refreshComponent() {
            // Do nothing
        }

        override fun getChilds() = listOf(this.widget.child, this.widget.dialog)

        private fun proxyRenderedComponent() {
            renderedComponent = context.childs[0].element.renderedComponent
        }
    }
}
