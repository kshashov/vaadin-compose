package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import io.github.kshashov.vaadincompose.BuildContext

abstract class Element<WIDGET : Widget>(var widget: WIDGET) {
    lateinit var context: BuildContext
        private set
    lateinit var renderedComponent: Component
        protected set
    var state: State = State.CREATED
        private set

    /**
     * Adds element to the current build context and populate [renderedComponent] property.
     * Invoked before the first element usage.
     */
    fun mount(parent: BuildContext) {
        attachContext(parent)
        onBeforeRender()
        renderedComponent = render(context)
        onAfterRender()
        state = State.MOUNTED
    }

    /**
     * Creates and configures component for this element.
     */
    protected abstract fun render(context: BuildContext): Component

    /**
     * Refreshes element state according to a new [widget] info.
     */
    fun attachWidget(widget: Widget) {
        if (!state.equals(State.DETACHED) && !state.equals(State.MOUNTED) && !state.equals(State.REMOUNTED)) {
            throw IllegalStateException("Element State should be DETACHED or MOUNTED before re-attach")
        }
        state = State.ATTACHED
        onBeforeWidgetRefresh()
        doAttachWidget(widget)
        onAfterWidgetRefresh()

        state = State.REMOUNTED
    }

    protected open fun key(widget: Widget, index: Int): String {
        return widget.key ?: widget.javaClass.name + index
    }

    private fun attachContext(parent: BuildContext) {
        if (!state.equals(State.CREATED)) {
            throw IllegalStateException("Element State should be CREATED before first attach")
        }
        context = BuildContext.child(this, parent = parent)
        parent.childs.add(context)
        state = State.ATTACHED
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun doAttachWidget(widget: Widget) {
        this.widget = widget as WIDGET;
    }

    /**
     * Invoked when the element is detached from tree but still stored in cache.
     */
    open fun detach() {
        if (!state.equals(State.MOUNTED) && !state.equals(State.REMOUNTED)) {
            throw IllegalStateException("Element State should be MOUNTED before detach")
        }
        state = State.DETACHED
    }

    /**
     * Invoked after the [detach] method if the element is completely removed from the hierarchy.
     */
    open fun dispose() {
        if (!state.equals(State.DETACHED)) {
            throw IllegalStateException("Element State should be DETACHED before detach")
        }
        state = State.DISPOSED
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

    enum class State {
        CREATED,
        ATTACHED,
        MOUNTED,
        REMOUNTED,
        DETACHED,
        DISPOSED
    }
}
