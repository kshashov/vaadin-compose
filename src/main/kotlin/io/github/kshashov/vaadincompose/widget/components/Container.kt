package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.orderedlayout.FlexLayout
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.HasChildRenderElement
import io.github.kshashov.vaadincompose.widget.RenderWidget
import io.github.kshashov.vaadincompose.widget.Widget

class Container(
    val childs: List<Widget>,
    val direction: FlexLayout.FlexDirection = FlexLayout.FlexDirection.ROW,
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null,
    postProcess: ((FlexLayout) -> Unit)? = null
) : RenderWidget<FlexLayout>(key, height, width, id, classes, alignItems, justifyContent, postProcess) {

    override fun createElement(): Element<Container> {
        return ContainerRenderElement(this)
    }

    class ContainerRenderElement(widget: Container) : HasChildRenderElement<Container, FlexLayout>(widget) {

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
