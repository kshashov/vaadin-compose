package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import io.github.kshashov.vaadincompose.BuildContext

abstract class MultiChildRenderElement<WIDGET, COMPONENT>(widget: WIDGET) : RenderElement<WIDGET, COMPONENT>(widget)
        where WIDGET : RenderWidget, COMPONENT : Component, COMPONENT : HasComponents {
    private val elementsCache: MutableMap<String, Element<*>> = HashMap()

    override fun render(context: BuildContext): Component {
        component = createComponent()
        updateContextChilds(getChilds())
        update()
        return component
    }

    override fun updateContext(widget: Widget) {
        super.updateContext(widget)
        updateContextChilds(getChilds())
    }

    abstract fun getChilds(): Collection<Widget>

    /**
     * Update context.childs metainfo
     */
    protected fun updateContextChilds(list: Collection<Widget>) {
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
                cachedElement.updateContext(childWidget)
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
        elementsCache.keys.removeIf {
            !actualCacheKeys.contains(it)
        }
    }

    override fun update() {
        super.update()

        // TODO do not remove component childs
        component.removeAll()

        // Populate component according actual childs
        for (childContext in context.childs) {
            val childComponent = childContext.element.renderedComponent
            component.add(childComponent)
        }
    }

    protected fun key(widget: Widget, index: Int): String {
        return widget.key ?: widget.javaClass.name + index
    }
}

