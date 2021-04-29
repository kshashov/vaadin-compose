package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import io.github.kshashov.vaadincompose.BuildContext

/**
 * Supposed to be used as a parent class for non-render widgets.
 * Propagates render shit to the nested widget.
 */
abstract class ProxyElement<WIDGET : Widget>(widget: WIDGET) : Element<WIDGET>(widget) {
    protected val elementsCache: MutableMap<String, Element<*>> = HashMap()

    override fun render(context: BuildContext): Component {
        return renderedComponent
    }

    override fun onBeforeRender() {
        super.onBeforeRender()
        updateContextChilds(getChild())
    }

    override fun onAfterWidgetRefresh() {
        super.onAfterWidgetRefresh()
        updateContextChilds(getChild())
    }

    abstract fun getChild(): Widget

    /**
     * Update context.childs metainfo. Tries to reuse elements for known widget key.
     */
    protected open fun updateContextChilds(child: Widget) {
        context.childs.clear()

        // Store useful cache keys
        val actualCacheKey: String

        val childKey = key(child, 0)
        actualCacheKey = childKey

        // Try to reuse element from cache
        if (elementsCache.containsKey(childKey)) {
            val cachedElement = elementsCache[childKey]!!

            // We shouldn't mount existing element here
            // Just propagate context updating to child
            cachedElement.attachWidget(child)
            renderedComponent = cachedElement.renderedComponent
            context.childs.add(cachedElement.context)
        } else {
            // Create and mount new element
            // Mount updates the current context childs so we shouldn't do it manually
            val childElement = child.createElement()
            childElement.mount(context)
            renderedComponent = childElement.renderedComponent
            elementsCache[childKey] = childElement
        }

        // Remove cache keys that are not in a prepopulated set
        elementsCache.keys.removeIf {
            actualCacheKey != it
        }
    }
}

