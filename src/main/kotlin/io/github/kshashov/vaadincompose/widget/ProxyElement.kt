package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import io.github.kshashov.vaadincompose.BuildContext

/**
 * Supposed to be used as a parent class for non-render widgets.
 * Propagates render shit to the nested widget.
 */
abstract class ProxyElement<WIDGET : Widget>(widget: WIDGET) : Element<WIDGET>(widget), HasDebugInfo {
    protected val elementsCache: MutableMap<String, Element<*>> = HashMap()

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
        elementsCache.entries.removeIf {
            val check = actualCacheKey != it.key
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
     * Returns true if the element should be stored in the cache to be reused later.
     */
    protected open fun shouldKeepDetachedElement(entry: Map.Entry<String, Element<*>>): Boolean {
        return false
    }

    override fun getDebugInfo(): Map<String, Any>? {
        return elementsCache.entries.associate { "cache/" + it.key to it.value }
    }
}

