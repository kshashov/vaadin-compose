package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.dom.Style
import io.github.kshashov.vaadincompose.HasChildWidgets
import io.github.kshashov.vaadincompose.widget.EagerChildsRenderElement
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.RenderWidget
import io.github.kshashov.vaadincompose.widget.Widget

class Container(
    val childs: MutableList<Widget>,
    val direction: FlexLayout.FlexDirection = FlexLayout.FlexDirection.ROW,
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null,
    styleProcess: ((style: Style) -> Unit)? = null,
    postProcess: ((component: FlexLayout) -> Unit)? = null
) : RenderWidget<FlexLayout>(key, height, width, id, classes, alignItems, justifyContent, styleProcess, postProcess),
    HasChildWidgets {

    override fun createElement(): Element<Container> {
        return ContainerRenderElement(this)
    }

    override fun add(widget: Widget) {
        childs.add(widget)
    }

    class ContainerRenderElement(widget: Container) : EagerChildsRenderElement<Container, FlexLayout>(widget) {

        override fun createComponent(): FlexLayout {
            return FlexLayout()
        }

        override fun refreshComponent() {
            super.refreshComponent()
            component.setFlexDirection(widget.direction)
        }

        override fun getChilds() = this.widget.childs
    }
}
