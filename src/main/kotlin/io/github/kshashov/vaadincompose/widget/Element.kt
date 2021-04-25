package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import io.github.kshashov.vaadincompose.BuildContext

abstract class Element<WIDGET : Widget>(var widget: WIDGET) {
    lateinit var context: BuildContext

    fun attachContext(context: BuildContext): BuildContext {
        val node = BuildContext(this, parent = context)
        context.childs.add(node)
        this.context = node
        return node
    }

    fun mount(context: BuildContext): Component {
        val node = attachContext(context)
        return render(node)
    }

    abstract fun render(context: BuildContext): Component

    @Suppress("UNCHECKED_CAST")
    open fun updateWidgetContext(widget: Widget) {
        this.widget = widget as WIDGET;
    }
}
