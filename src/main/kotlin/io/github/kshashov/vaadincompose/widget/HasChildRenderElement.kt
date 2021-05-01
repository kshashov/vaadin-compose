package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents

abstract class HasChildRenderElement<WIDGET, COMPONENT>(widget: WIDGET) : RenderElement<WIDGET, COMPONENT>(widget)
        where WIDGET : RenderWidget, COMPONENT : Component, COMPONENT : HasComponents {
    protected val elementsCache: MutableMap<String, Element<*>> = HashMap()

    protected abstract fun getChilds(): Collection<Widget>

    override fun onBeforeRender() {
        super.onBeforeRender()
        updateContextChilds(getChilds())
    }

    override fun onAfterWidgetRefresh() {
        super.onAfterWidgetRefresh()
        updateContextChilds(getChilds())
    }

    /**
     * Updates context.childs metainfo.
     * Puts cached elements to the three or mounts new nodes for unknown child widgets.
     */
    protected open fun updateContextChilds(list: Collection<Widget>) {
        context.childs.clear()

        // Store useful cache keys
        val actualCacheKeys = HashSet<String>()

        // Iterate for all child widgets
        for ((i, childWidget) in list.withIndex()) {
            val childKey = key(childWidget, i)
            actualCacheKeys.add(childKey)

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

        // Remove cache keys that are not in a prepopulated set
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

    /**
     * Returns true if the element should be stored in the cache to be reused later.
     */
    protected open fun shouldKeepDetachedElement(mutableEntry: Map.Entry<String, Element<*>>): Boolean {
        return false
    }

    override fun refreshComponent() {
        super.refreshComponent()

        // TODO do not remove component childs
        component.removeAll()

        // Populate component according actual childs
        for (childContext in context.childs) {
            if (shouldAddElement(childContext.element)) {
                val childComponent = childContext.element.renderedComponent
                component.add(childComponent)
            }
        }
    }

    /**
     * Returns true if the rendered component should be added to the layout.
     */
    protected open fun shouldAddElement(element: Element<*>): Boolean {
        return true
    }
}

