package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.shared.Registration
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.RenderElement
import io.github.kshashov.vaadincompose.widget.RenderWidget
import com.vaadin.flow.component.button.Button as VaadinButton

class Button(
    val text: String = "",
    val action: (() -> Unit)? = null,
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null,
    postProcess: ((component: VaadinButton) -> Unit)? = null
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

    class ButtonRenderElement(widget: Button) : RenderElement<Button, VaadinButton>(widget) {
        private var reg: Registration? = null

        override fun createComponent(): VaadinButton {
            return VaadinButton()
        }

        override fun refreshComponent() {
            super.refreshComponent();

            reg?.remove()
            component.text = widget.text

            val action = widget.action
            if (action != null) {
                reg = component.addClickListener { action.invoke() }
            }
        }
    }
}
