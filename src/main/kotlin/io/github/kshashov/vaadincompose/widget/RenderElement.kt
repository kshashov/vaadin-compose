package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Tag
import com.vaadin.flow.dom.ElementConstants
import io.github.kshashov.vaadincompose.BuildContext

abstract class RenderElement<WIDGET : RenderWidget<COMPONENT>, COMPONENT : Component>(widget: WIDGET) :
    Element<WIDGET>(widget) {
    protected lateinit var component: COMPONENT
    var dirty: Boolean = false

    override fun render(context: BuildContext): Component {
        component = createComponent()

        refreshComponent()
        postProcess()

        return component
    }

    protected abstract fun createComponent(): COMPONENT

    open fun refreshComponent() {
        component.setId(widget.id)

        val dom = component.element
        dom.classList.clear()
        dom.classList.addAll(widget.classes)

        dom.style.set(ElementConstants.STYLE_HEIGHT, widget.height)
        dom.style.set(ElementConstants.STYLE_WIDTH, widget.width)
        dom.style.set("align-items", widget.alignItems)
        dom.style.set("justify-content", widget.justifyContent)
    }

    open fun postProcess() {
        widget.postProcess?.invoke(component)
    }

    @Tag("v-compose-empty")
    class EmptyWidget : Component()

    companion object {
        val EMPTY_WIDGET = EmptyWidget()
    }
}
