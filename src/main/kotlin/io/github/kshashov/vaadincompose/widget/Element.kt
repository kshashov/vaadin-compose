package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import io.github.kshashov.vaadincompose.BuildContext

abstract class Element<WIDGET : Widget>(var widget: WIDGET) {
    lateinit var context: BuildContext
    lateinit var renderedComponent: Component

    private fun attachContext(context: BuildContext): BuildContext {
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

    protected abstract fun render(context: BuildContext): Component

    fun updateContext(widget: Widget) {
        onBeforeContextRefresh()
        doUpdateContext(widget)
        onAfterContextRefresh()
    }

    protected open fun key(widget: Widget, index: Int): String {
        return widget.key ?: widget.javaClass.name + index
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun doUpdateContext(widget: Widget) {
        this.widget = widget as WIDGET;
    }

    protected open fun onBeforeContextRefresh() {
        // Do nothing
    }

    protected open fun onAfterContextRefresh() {
        // Do nothing
    }
}
