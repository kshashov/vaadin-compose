package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.orderedlayout.FlexLayout
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.HasChildRenderElement
import io.github.kshashov.vaadincompose.widget.RenderWidget
import io.github.kshashov.vaadincompose.widget.Widget
import kotlin.streams.toList

class ListView<T>(
    val items: Collection<T>,
    val render: (item: T) -> Widget,
    val direction: FlexLayout.FlexDirection = FlexLayout.FlexDirection.ROW,
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null
) : RenderWidget(key, height, width, id, classes, alignItems, justifyContent) {

    override fun createElement(): Element<ListView<T>> {
        return ContainerRenderElement(this)
    }

    class ContainerRenderElement<T>(widget: ListView<T>) : HasChildRenderElement<ListView<T>, FlexLayout>(widget) {

        override fun createComponent(): FlexLayout {
            return FlexLayout()
        }

        override fun refreshComponent() {
            super.refreshComponent()
            component.setFlexDirection(widget.direction)
        }

        override fun getChilds() = this.widget.items.stream()
            .map { item -> widget.render.invoke(item) }
            .toList()
    }
}
