package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import io.github.kshashov.vaadincompose.BuildContext

abstract class Element<WIDGET : Widget>(var widget: WIDGET) {
    lateinit var context: BuildContext
    lateinit var renderedComponent: Component

    fun attachContext(context: BuildContext): BuildContext {
        val node = BuildContext(this, parent = context)
        context.childs.add(node)
        this.context = node
        return node
    }

    fun mount(context: BuildContext): Component {
        val node = attachContext(context)
        renderedComponent = render(node)
        return renderedComponent
    }

    abstract fun render(context: BuildContext): Component

    protected fun key(widget: Widget, index: Int): String {
        return widget.key ?: widget.javaClass.name + index
    }

    @Suppress("UNCHECKED_CAST")
    open fun updateContext(widget: Widget) {
        this.widget = widget as WIDGET;
    }
}
