package io.github.kshashov.vaadincompose.widget

import io.github.kshashov.vaadincompose.BuildContext

/**
 * Utility mixin to simplify interaction between element and his nested childs.
 * Provides an ability to store nested elements in cache.
 */
interface HasChildsElement : HasDebugCacheInfoElement {

    /**
     * Remove obsolete entries from [getElementsCache] element cache.
     * Obsolete elements are become [Element.State.DETACHED] or [Element.State.DISPOSED] depending on [shouldKeepDetachedElement] result.
     */
    fun removeObsoleteCache(actualCacheKeys: Set<String>) {
        val elementsCache = getElementsCache()

        elementsCache.entries.removeIf {
            val check = !actualCacheKeys.contains(it.key)
            if (check) {
                // Detach if is not detached yet
                if (!it.value.state.equals(Element.State.DETACHED)) {
                    it.value.detach()
                }

                // Give element a chance to be reused later
                if (shouldKeepDetachedElement(it)) {
                    return@removeIf false
                }
                it.value.dispose()
            }
            return@removeIf check
        }
    }

    /**
     * Attach cached element to the three or mounts a new node for unknown child widgets.
     */
    fun updateContextChild(context: BuildContext, childKey: String, childWidget: Widget): Element<*> {
        val element: Element<*>
        val elementsCache = getElementsCache()

        // Try to reuse element from cache
        if (elementsCache.containsKey(childKey)) {
            element = elementsCache[childKey]!!

            // We shouldn't mount existing element here
            // Just propagate context updating to child
            element.attachWidget(childWidget)
            context.childs.add(element.context)
        } else {
            // Create and mount new element
            // Mount updates the current context childs so we shouldn't do it manually
            element = childWidget.createElement()
            element.mount(context)
            elementsCache[childKey] = element
        }

        return element
    }

    fun getElementsCache(): MutableMap<String, Element<*>>

    /**
     * Returns true if the element should be stored in the cache to be reused later.
     */
    fun shouldKeepDetachedElement(entry: Map.Entry<String, Element<*>>): Boolean {
        return false
    }

    override fun getDebugCacheInfo() = getElementsCache().toMap()
}

