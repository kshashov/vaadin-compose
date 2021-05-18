package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents

abstract class EagerChildsRenderElement<WIDGET : RenderWidget<COMPONENT>, COMPONENT : Component>(widget: WIDGET) :
    RenderElement<WIDGET, COMPONENT>(widget), HasChildsElement {
    private val cache: MutableMap<String, Element<*>> = HashMap()

    override fun getElementsCache() = cache

    protected abstract fun getChilds(): Collection<Widget>

    override fun onBeforeRender() {
        super.onBeforeRender()
        updateContextChilds(getChilds())
    }

    override fun doAttachWidget(widget: Widget) {
        super.doAttachWidget(widget)
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

            updateContextChild(context, childKey, childWidget)
        }

        // Remove cache keys that are not in a prepopulated set
        removeObsoleteCache(actualCacheKeys)
    }

    override fun refreshComponent() {
        super.refreshComponent()

        // Populate component according actual childs
        var index = 0
        for (newChildContext in context.childs) {
            if (shouldAddElement(newChildContext.element)) {
                val newComponent = newChildContext.element.renderedComponent

                // We already have a component at this index
                if (index < getComponentsCount()) {
                    val oldComponent = getComponentAtIndex(index)

                    // Replace old component with a new instance
                    if (!oldComponent.equals(newComponent)) {
                        replaceComponentAtIndex(index, oldComponent, newComponent);
                    }
                } else {
                    addComponentAtIndex(index, newComponent)
                }

                index++;
            }
        }

        // Remove extra components
        for (left in context.childs.size until getComponentsCount()) {
            removeExtraComponentAtIndex(left, getComponentAtIndex(left))
        }
    }

    protected abstract fun getComponentsCount(): Int
    protected abstract fun getComponentAtIndex(index: Int): Component
    protected abstract fun addComponentAtIndex(index: Int, newComponent: Component)
    protected abstract fun replaceComponentAtIndex(index: Int, oldComponent: Component, newComponent: Component)
    protected abstract fun removeExtraComponentAtIndex(index: Int, componentAtIndex: Component)

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

