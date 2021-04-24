package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.orderedlayout.FlexLayout
import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.RenderElement
import io.github.kshashov.vaadincompose.widget.Widget
import java.util.stream.Collectors

class Container(
        var components: Collection<Widget>,
        key: String? = null
) : Widget(key) {

    override fun createElement(): Element<Container> {
        return ContainerRenderElement(this)
    }

    class ContainerRenderElement(widget: Container) : RenderElement<Container>(widget) {
        override fun render(context: BuildContext): Component {
            val layout = FlexLayout();
            val content = widget.components.stream()
                    .map { it.createElement().render(context) }
                    .collect(Collectors.toUnmodifiableList())
                    .toTypedArray()

            layout.add(*content)
            return layout
        }
    }
}
