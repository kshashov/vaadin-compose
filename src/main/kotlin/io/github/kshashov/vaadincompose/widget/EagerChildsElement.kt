package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import io.github.kshashov.vaadincompose.BuildContext

/**
 * Supposed to be used as a parent class for non-render widgets to proxy rendered components.
 * Propogates render shit from the nested child at [proxiedComponentIndex] position.
 */
abstract class EagerChildsElement<WIDGET : Widget>(widget: WIDGET) : Element<WIDGET>(widget), HasChildsElement {
    private val cache: MutableMap<String, Element<*>> = HashMap()

    override fun getElementsCache() = cache

    override fun render(context: BuildContext): Component {
        return if (proxiedComponentIndex() >= 0) renderedComponent else RenderElement.EMPTY_WIDGET
    }

    protected abstract fun getChilds(): Collection<Widget>

    override fun onBeforeRender() {
        super.onBeforeRender()
        init()
        updateContextChilds(getChilds())
    }

    override fun doAttachWidget(widget: Widget) {
        super.doAttachWidget(widget)
        didUpdateWidget()
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

        val proxiedComponentIndex = proxiedComponentIndex()
        if (proxiedComponentIndex < 0 || proxiedComponentIndex >= list.size) {
            throw IllegalStateException("proxiedComponentIndex should returns child index from 0 to childs count")
        }

        // Iterate for all child widgets
        for ((i, childWidget) in list.withIndex()) {
            val childKey = key(childWidget, i)
            actualCacheKeys.add(childKey)

            val childElement = updateContextChild(context, childKey, childWidget)
            if (i == proxiedComponentIndex) {
                renderedComponent = childElement.renderedComponent
            }
        }

        // Remove cache keys that are not in a prepopulated set
        removeObsoleteCache(actualCacheKeys)
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


    /**
     * Number of child element to proxy it's rendered component.
     *
     * @returns index of child or negative number if no components should be proxied.
     */
    abstract fun proxiedComponentIndex(): Int
}

