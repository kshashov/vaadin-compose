package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.Component
import io.github.kshashov.vaadincompose.widget.EagerChildsRenderElement
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.RenderWidget
import io.github.kshashov.vaadincompose.widget.Widget
import com.vaadin.flow.component.dialog.Dialog as VaadinDialog

/**
 * Should be used as a child for [HasDialog] widget.
 */
class Dialog(
    private val child: Widget,
    private val show: Boolean,
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null,
    postProcess: (VaadinDialog.() -> Unit)? = null,
) : RenderWidget<VaadinDialog>(key, height, width, id, classes, alignItems, justifyContent, postProcess) {

    override fun createElement(): Element<Dialog> {
        return DialogRenderElement(this)
    }

    internal class DialogRenderElement(widget: Dialog) : EagerChildsRenderElement<Dialog, VaadinDialog>(widget) {

        override fun createComponent(): VaadinDialog {
            return VaadinDialog()
        }

        override fun refreshComponent() {
            super.refreshComponent()
            component.isOpened = widget.show
        }

        override fun getChilds() = listOf(this.widget.child)

        override fun getComponentsCount(): Int {
            return component.children.count().toInt()
        }

        override fun getComponentAtIndex(index: Int): Component {
            return component.children.findFirst().get()
        }

        override fun replaceComponentAtIndex(index: Int, oldComponent: Component, newComponent: Component) {
            component.removeAll()
            component.add(newComponent)
        }

        override fun addComponentAtIndex(index: Int, newComponent: Component) {
            component.add(newComponent)
        }

        override fun removeExtraComponentAtIndex(index: Int, componentAtIndex: Component) {
            // We always have a single child here!
            throw NotImplementedError()
        }
    }
}
