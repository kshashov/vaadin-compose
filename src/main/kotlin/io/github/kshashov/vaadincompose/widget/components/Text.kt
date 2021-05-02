package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.shared.Registration
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.RenderElement
import io.github.kshashov.vaadincompose.widget.RenderWidget

class Text(
    val text: String = "",
    val label: String = "",
    val onChanged: (String) -> Unit = { },
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null,
    postProcess: ((TextField) -> Unit)? = null
) : RenderWidget<TextField>(key, height, width, id, classes, alignItems, justifyContent, postProcess) {

    override fun createElement(): Element<Text> {
        return TextRenderElement(this)
    }

    class TextRenderElement(widget: Text) : RenderElement<Text, TextField>(widget) {
        private var reg: Registration? = null

        override fun createComponent(): TextField {
            return TextField()
        }

        override fun refreshComponent() {
            super.refreshComponent();

            reg?.remove()

            component.label = widget.label
            component.value = widget.text
            reg = component.addValueChangeListener {
                widget.onChanged.invoke(it.value)
            }
        }
    }
}
