package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import io.github.kshashov.vaadincompose.BuildContext

/**
 * Supposed to be used as a parent class for non-render widgets.
 * Propagates render shit from the nested widget.
 */
abstract class ProxyElement<WIDGET : Widget>(widget: WIDGET) : Element<WIDGET>(widget), HasChildsElement {
    private val cache: MutableMap<String, Element<*>> = HashMap()

    override fun getElementsCache() = cache

    override fun render(context: BuildContext): Component {
        return renderedComponent
    }

    override fun onBeforeRender() {
        super.onBeforeRender()
        init()
        updateContextChilds(getChild())
    }

    override fun onAfterWidgetRefresh() {
        super.onAfterWidgetRefresh()
        didUpdateWidget()
        updateContextChilds(getChild())
    }

    abstract fun getChild(): Widget

    /**
     * Update context.childs metainfo. Tries to reuse elements for known widget keys.
     */
    protected open fun updateContextChilds(child: Widget) {
        context.childs.clear()

        val childKey = key(child, 0)

        val childElement = updateContextChild(context, childKey, child)
        renderedComponent = childElement.renderedComponent

        removeObsoleteCache(setOf(childKey))
    }

    /**
     * Invoked before first context build.
     */
    protected open fun init() {
    }

    /**
     * Invoked before context updating.
     */
    protected open fun didUpdateWidget() {
    }
}

