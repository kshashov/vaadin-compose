package io.github.kshashov.vaadincompose.widget.components

import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.RenderElement
import io.github.kshashov.vaadincompose.widget.RenderWidget

class Label(
    val text: String = "",
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null
) : RenderWidget(key, height, width, id, classes, alignItems, justifyContent) {

    override fun createElement(): Element<Label> {
        return LabelRenderElement(this)
    }

    class LabelRenderElement(widget: Label) : RenderElement<Label, com.vaadin.flow.component.html.Label>(widget) {
        override fun createComponent(): com.vaadin.flow.component.html.Label {
            return com.vaadin.flow.component.html.Label()
        }

        override fun refresh() {
            super.refresh();
            component.text = widget.text
        }
    }
}
