package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.Component
import io.github.kshashov.vaadincompose.widget.EagerChildsRenderElement
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.RenderWidget
import io.github.kshashov.vaadincompose.widget.Widget
import com.vaadin.flow.component.splitlayout.SplitLayout as VaadinSplitLayout

class SplitLayout(
    val primary: Widget,
    val secondary: Widget,
    val direction: VaadinSplitLayout.Orientation? = VaadinSplitLayout.Orientation.HORIZONTAL,
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null,
    postProcess: (VaadinSplitLayout.() -> Unit)? = null
) : RenderWidget<VaadinSplitLayout>(
    key,
    height,
    width,
    id,
    classes,
    alignItems,
    justifyContent,
    postProcess
) {

    override fun createElement(): Element<SplitLayout> {
        return SplitLayoutRenderElement(this)
    }

    internal class SplitLayoutRenderElement(widget: SplitLayout) :
        EagerChildsRenderElement<SplitLayout, VaadinSplitLayout>(widget) {

        override fun createComponent(): VaadinSplitLayout {
            return VaadinSplitLayout()
        }

        override fun refreshComponent() {
            super.refreshComponent()
            component.orientation = widget.direction
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
