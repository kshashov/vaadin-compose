package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.dom.Style
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.RenderElement
import io.github.kshashov.vaadincompose.widget.RenderWidget
import com.vaadin.flow.component.html.Label as VaadinLabel

class Label(
    val text: String = "",
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null,
    styleProcess: ((style: Style) -> Unit)? = null,
    postProcess: ((component: VaadinLabel) -> Unit)? = null
) : RenderWidget<VaadinLabel>(key, height, width, id, classes, alignItems, justifyContent, styleProcess, postProcess) {

    override fun createElement(): Element<Label> {
        return LabelRenderElement(this)
    }

    class LabelRenderElement(widget: Label) : RenderElement<Label, VaadinLabel>(widget) {
        override fun createComponent(): VaadinLabel {
            return VaadinLabel()
        }

        override fun refreshComponent() {
            super.refreshComponent();
            component.text = widget.text
        }
    }
}
