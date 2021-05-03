package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component

abstract class ChildSupportRenderElement<WIDGET : RenderWidget<COMPONENT>, COMPONENT : Component>(widget: WIDGET) :
    RenderElement<WIDGET, COMPONENT>(widget) {
    protected val elementsCache: MutableMap<String, Element<*>> = HashMap()

    /**
     * Attach cached element to the three or mounts a new node for unknown child widgets.
     */
    protected open fun removeObsoleteCache(actualCacheKeys: HashSet<String>) {
        elementsCache.entries.removeIf {
            val check = !actualCacheKeys.contains(it.key)
            if (check) {
                it.value.detach()
                // Give element a chance to be reused later
                if (shouldKeepDetachedElement(it)) {
                    return@removeIf false
                }
                it.value.dispose()
            }
            return@removeIf check
        }
    }

    protected open fun updateContextChild(childKey: String, childWidget: Widget) {
        // Try to reuse element from cache
        if (elementsCache.containsKey(childKey)) {
            val cachedElement = elementsCache[childKey]!!

            // We shouldn't mount existing element here
            // Just propagate context updating to child
            cachedElement.attachWidget(childWidget)
            context.childs.add(cachedElement.context)
        } else {
            // Create and mount new element
            // Mount updates the current context childs so we shouldn't do it manually
            val childElement = childWidget.createElement()
            childElement.mount(context)
            elementsCache[childKey] = childElement
        }
    }

    /**
     * Returns true if the element should be stored in the cache to be reused later.
     */
    protected open fun shouldKeepDetachedElement(mutableEntry: Map.Entry<String, Element<*>>): Boolean {
        return false
    }
}

