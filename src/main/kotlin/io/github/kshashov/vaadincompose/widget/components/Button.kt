package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.shared.Registration
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.RenderElement
import io.github.kshashov.vaadincompose.widget.RenderWidget

class Button(
    val text: String = "",
    val action: () -> Unit = {},
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null
) : RenderWidget(key, height, width, id, classes, alignItems, justifyContent) {

    override fun createElement(): Element<Button> {
        return ButtonRenderElement(this)
    }

    class ButtonRenderElement(widget: Button) : RenderElement<Button, com.vaadin.flow.component.button.Button>(widget) {
        private var reg: Registration? = null

        override fun createComponent(): com.vaadin.flow.component.button.Button {
            return com.vaadin.flow.component.button.Button()
        }

        override fun refreshComponent() {
            super.refreshComponent();

            reg?.remove()
            component.text = widget.text
            reg = component.addClickListener { widget.action.invoke() }
        }
    }
}
