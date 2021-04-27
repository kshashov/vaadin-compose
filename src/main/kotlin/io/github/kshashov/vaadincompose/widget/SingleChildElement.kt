package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import io.github.kshashov.vaadincompose.BuildContext

abstract class SingleChildElement<WIDGET : Widget>(widget: WIDGET) : Element<WIDGET>(widget) {
    private val elementsCache: MutableMap<String, Element<*>> = HashMap()

    override fun render(context: BuildContext): Component {
        val child = getChild()

        // Proxy child component and populate cache
        val component = child.createElement().mount(context)
        elementsCache[key(child, 0)] = context.childs[0].element

        return component
    }

    override fun updateContext(widget: Widget) {
        super.updateContext(widget)
        updateContextChilds(getChild())
    }

    abstract fun getChild(): Widget

    /**
     * Update context.childs metainfo
     */
    protected fun updateContextChilds(child: Widget) {
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
            cachedElement.updateContext(child)
            context.childs.add(cachedElement.context)
        } else {
            // Create and mount new element
            // Mount updates the current context childs so we shouldn't do it manually
            val childElement = child.createElement()
            renderedComponent = childElement.mount(context)
            elementsCache[childKey] = childElement
        }

        // Remove cache keys that are not in a prepopulated set
        elementsCache.keys.removeIf {
            actualCacheKey != it
        }
    }
}

