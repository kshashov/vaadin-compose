package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.orderedlayout.FlexLayout
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.MultiChildRenderElement
import io.github.kshashov.vaadincompose.widget.RenderWidget
import io.github.kshashov.vaadincompose.widget.Widget

class Container(
    val components: List<Widget>,
    val direction: FlexLayout.FlexDirection = FlexLayout.FlexDirection.ROW,
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null
) : RenderWidget(key, height, width, id, classes, alignItems, justifyContent) {

    override fun createElement(): Element<Container> {
        return ContainerRenderElement(this)
    }

    class ContainerRenderElement(widget: Container) : MultiChildRenderElement<Container, FlexLayout>(widget) {

        override fun createComponent(): FlexLayout {
            return FlexLayout()
        }

        override fun refresh() {
            super.refresh()
            component.setFlexDirection(widget.direction)
        }

        override fun getChilds() = this.widget.components
    }
}
