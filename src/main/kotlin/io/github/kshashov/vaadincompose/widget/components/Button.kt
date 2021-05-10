package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.Component
import com.vaadin.flow.dom.Style
import com.vaadin.flow.shared.Registration
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.RenderElement
import io.github.kshashov.vaadincompose.widget.RenderWidget
import com.vaadin.flow.component.button.Button as VaadinButton

class Button(
    val text: String? = null,
    val onClick: (() -> Unit)? = null,
    val icon: Component? = null,
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null,
    styleProcess: ((style: Style) -> Unit)? = null,
    postProcess: ((component: VaadinButton) -> Unit)? = null
) : RenderWidget<VaadinButton>(
    key,
    height,
    width,
    id,
    classes,
    alignItems,
    justifyContent,
    styleProcess,
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

            if (widget.text != null) {
                component.text = widget.text!!
            }

            if (widget.icon != null) {
                component.icon = widget.icon!!
            }

            val action = widget.onClick
            if (action != null) {
                reg = component.addClickListener { action.invoke() }
            }
        }
    }
}
