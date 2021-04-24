package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.html.Label
import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.RenderElement
import io.github.kshashov.vaadincompose.widget.Widget

class Text(
        val text: String = "",
        key: String? = null
) : Widget(key) {

    override fun createElement(): Element<Text> {
        return ContainerRenderElement(this)
    }

    class ContainerRenderElement(widget: Text) : RenderElement<Text>(widget) {
        override fun render(context: BuildContext): Component {
            return Label(widget.text)
        }
    }
}
