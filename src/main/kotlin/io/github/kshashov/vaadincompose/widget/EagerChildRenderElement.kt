package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents

abstract class EagerChildRenderElement<WIDGET : RenderWidget<COMPONENT>, COMPONENT : Component>(widget: WIDGET) :
    ChildSupportRenderElement<WIDGET, COMPONENT>(widget) {

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

            updateContextChild(childKey, childWidget)
        }

        // Remove cache keys that are not in a prepopulated set
        removeObsoleteCache(actualCacheKeys)
    }

    override fun refreshComponent() {
        super.refreshComponent()

        // TODO do not remove component childs
        doRemoveAll()

        // Populate component according actual childs
        for (childContext in context.childs) {
            if (shouldAddElement(childContext.element)) {
                val childComponent = childContext.element.renderedComponent
                doAdd(childComponent)
            }
        }
    }

    protected open fun doRemoveAll() {
        (component as HasComponents).removeAll()
    }

    protected open fun doAdd(child: Component) {
        (component as HasComponents).add(child)
    }

    /**
     * Returns true if the rendered component should be added to the layout.
     */
    protected open fun shouldAddElement(element: Element<*>): Boolean {
        return true
    }
}

