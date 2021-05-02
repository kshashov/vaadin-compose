package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.Component
import io.github.kshashov.vaadincompose.widget.HasChildRenderElement
import io.github.kshashov.vaadincompose.widget.RenderWidget
import io.github.kshashov.vaadincompose.widget.Widget
import com.vaadin.flow.component.splitlayout.SplitLayout as VaadinSplitLayout

class SplitLayout(
    val primary: Widget,
    val secondary: Widget,
    val orientation: com.vaadin.flow.component.splitlayout.SplitLayout.Orientation? = com.vaadin.flow.component.splitlayout.SplitLayout.Orientation.HORIZONTAL,
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null,
    postProcess: ((VaadinSplitLayout) -> Unit)? = null
) : RenderWidget<VaadinSplitLayout>(key, height, width, id, classes, alignItems, justifyContent, postProcess) {

    override fun createElement(): SplitLayoutRenderElement {
        return SplitLayoutRenderElement(this)
    }

    class SplitLayoutRenderElement(widget: SplitLayout) :
        HasChildRenderElement<SplitLayout, VaadinSplitLayout>(widget) {

        override fun createComponent(): VaadinSplitLayout {
            return VaadinSplitLayout()
        }

        override fun refreshComponent() {
            super.refreshComponent()
        }

        override fun getChilds() = listOf(widget.primary, widget.secondary)

        override fun doRemoveAll() {
            component.removeAll()
        }

        override fun doAdd(child: Component) {
            if (component.element.childCount == 0) {
                component.addToPrimary(child)
            } else {
                component.addToSecondary(child)
            }
        }
    }
}
