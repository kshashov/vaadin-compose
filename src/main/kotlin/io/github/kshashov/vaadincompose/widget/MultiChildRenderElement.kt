package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import io.github.kshashov.vaadincompose.BuildContext

abstract class MultiChildRenderElement<WIDGET, COMPONENT>(widget: WIDGET) : RenderElement<WIDGET, COMPONENT>(widget)
        where WIDGET : RenderWidget, COMPONENT : Component, COMPONENT : HasComponents {
    private val componentsCache: MutableMap<String, Component> = HashMap()
    private val elementsCache: MutableMap<String, Element<*>> = HashMap()

    // TODO remove obsolete cache
    // TODO do not remove component childs

    override fun render(context: BuildContext): Component {
        component = createComponent()
        updateContextChilds(getChilds())
        update()
        return component
    }

    override fun updateWidgetContext(widget: Widget) {
        super.updateWidgetContext(widget)
        updateContextChilds(getChilds())
    }

    abstract fun getChilds(): Collection<Widget>

    protected fun updateContextChilds(list: Collection<Widget>) {
        context.childs.clear()

        for ((i, childWidget) in list.withIndex()) {
            val childKey = key(childWidget, i)
            if (elementsCache.containsKey(childKey)) {
                elementsCache[childKey]!!.updateWidgetContext(childWidget)
                context.childs.add(elementsCache[childKey]!!.context)
            } else {
                val childElement = childWidget.createElement()
                childElement.attachContext(context)
                elementsCache[childKey] = childElement
            }
        }
    }

    override fun update() {
        super.update()

        component.removeAll()

        for (i in 0 until context.childs.size) {
            val childContext = context.childs[i]
            val childKey = key(childContext.element!!.widget, i)
            if (componentsCache.containsKey(childKey)) {
                component.add(componentsCache[childKey])
            } else {
                val childComponent = childContext.element.render(childContext)
                component.add(childComponent)
                componentsCache[childKey] = childComponent
            }
        }
    }

    protected fun key(widget: Widget, index: Int): String {
        return (widget.key ?: widget.javaClass.name) + index
    }
}

