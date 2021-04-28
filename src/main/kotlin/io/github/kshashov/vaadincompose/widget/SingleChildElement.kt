package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import io.github.kshashov.vaadincompose.BuildContext

abstract class SingleChildElement<WIDGET : Widget>(widget: WIDGET) : Element<WIDGET>(widget) {
    private val elementsCache: MutableMap<String, Element<*>> = HashMap()
    private lateinit var rendered: Component

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
     * Update context.childs metainfo.
     */
    private fun updateContextChilds(child: Widget) {
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

