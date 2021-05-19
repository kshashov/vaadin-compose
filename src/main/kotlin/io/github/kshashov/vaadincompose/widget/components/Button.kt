package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.Component
import com.vaadin.flow.shared.Registration
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.RenderElement
import io.github.kshashov.vaadincompose.widget.RenderWidget
import com.vaadin.flow.component.button.Button as VaadinButton

class Button(
    private val text: String? = null,
    private val onClick: (() -> Unit)? = null,
    private val icon: Component? = null,
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null,
    postProcess: (VaadinButton.() -> Unit)? = null
) : RenderWidget<VaadinButton>(
    key,
    height,
    width,
    id,
    classes,
    alignItems,
    justifyContent,
    postProcess
) {

    override fun createElement(): Element<Button> {
        return ButtonRenderElement(this)
    }

    internal class ButtonRenderElement(widget: Button) : RenderElement<Button, VaadinButton>(widget) {
        private var reg: Registration? = null

        override fun createComponent(): VaadinButton {
            return VaadinButton()
        }

        override fun refreshComponent() {
            super.refreshComponent();

            if (widgetPropertyIsChanged { it.text }) {
                component.text = widget.text
            }

            if (widgetPropertyIsChanged { it.icon }) {
                component.icon = widget.icon
            }

            if (widgetPropertyIsChanged { it.onClick }) {
                reg?.remove()

                val action = widget.onClick
                if (action != null) {
                    reg = component.addClickListener { action.invoke() }
                }
            }
        }
    }
}
