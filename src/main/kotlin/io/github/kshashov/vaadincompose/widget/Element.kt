package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import io.github.kshashov.vaadincompose.BuildContext

abstract class Element<WIDGET : Widget>(var widget: WIDGET) {
    lateinit var context: BuildContext
    lateinit var renderedComponent: Component

    /**
     * Adds element to the current build context and populate [renderedComponent] property.
     * Invoked before the first element usage.
     */
    fun mount(parent: BuildContext): Component {
        attachContext(parent)
        onBeforeRender()
        renderedComponent = render(context)
        onAfterRender()
        return renderedComponent
    }

    /**
     * Creates and configures component for this element.
     */
    protected abstract fun render(context: BuildContext): Component

    /**
     * Refreshes element state accoring to a new [widget] info.
     */
    fun attachWidget(widget: Widget) {
        onBeforeWidgetRefresh()
        doAttachWidget(widget)
        onAfterWidgetRefresh()
    }

    protected open fun key(widget: Widget, index: Int): String {
        return widget.key ?: widget.javaClass.name + index
    }

    private fun attachContext(parent: BuildContext) {
        context = BuildContext(this, parent = parent)
        parent.childs.add(context)
    }

    @Suppress("UNCHECKED_CAST")
    private fun doAttachWidget(widget: Widget) {
        this.widget = widget as WIDGET;
    }

    protected open fun onBeforeRender() {
        // Do nothing
    }

    protected open fun onAfterRender() {
        // Do nothing
    }

    protected open fun onBeforeWidgetRefresh() {
        // Do nothing
    }

    protected open fun onAfterWidgetRefresh() {
        // Do nothing
    }
}
