package io.github.kshashov.vaadincompose

import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.Widget

class BuildContext(
    val element: Element<*>,
    val childs: MutableList<BuildContext> = mutableListOf(),
    val parent: BuildContext? = null,
    val listeners: MutableList<TreeListener> = mutableListOf()
) {
    @Suppress("UNCHECKED_CAST")
    fun <T : Element<*>> findParentElementByType(type: Class<T>): T? {
        return findParentElement {
            type.isAssignableFrom(it.javaClass)
        } as T?
    }

    @Suppress("UNCHECKED_CAST")
    fun findParentWidgetByKey(key: String): Widget? {
        return findParentElement {
            it.widget.key == key
        }?.widget
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Widget> findParentWidgetByType(type: Class<T>): T? {
        val widget = findParentElement {
            type.isAssignableFrom(it.widget.javaClass)
        }?.widget

        return widget as T?
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Element<*>> findChildElementByType(type: Class<T>): T? {
        return findChildElement {
            type.isAssignableFrom(it.javaClass)
        } as T?
    }

    @Suppress("UNCHECKED_CAST")
    fun findChildWidgetByKey(key: String): Widget? {
        return findChildElement {
            it.widget.key == key
        }?.widget
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Widget> findChildWidgetByType(type: Class<T>): T? {
        val widget = findChildElement {
            type.isAssignableFrom(it.widget.javaClass)
        }?.widget

        return widget as T?
    }

    fun findChildElement(filter: (element: Element<*>) -> Boolean): Element<*>? {
        if (filter.invoke(element)) {
            return element
        }

        for (element in childs) {
            val result = element.findChildElement(filter)
            if (result != null) {
                return result
            }
        }

        return null
    }

    fun findParentElement(filter: (element: Element<*>) -> Boolean): Element<*>? {
        if (filter.invoke(element)) {
            return element
        }

        return parent?.findParentElement(filter)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> visitChildElementsByType(type: Class<T>, action: (T) -> Unit) {
        if (type.isAssignableFrom(element.javaClass)) {
            action.invoke(element as T)
        }

        for (child in childs) {
            child.visitChildElementsByType(type, action)
        }
    }

    fun notify(code: String) {
        listeners.forEach { it.invoke(this, code) }
    }

    fun addListener(listener: TreeListener) {
        listeners.add(listener)
    }

    fun dispose() {
        for (context in childs) {
            context.dispose()
        }
        element.dispose()
    }

    /**
     * Fake Element that supposed to be used only in the root [BuildContext] node.
     */
    class FakeElement : Element<FakeElement.FakeWidget>(FakeWidget()) {
        override fun render(context: BuildContext) = TODO("Not yet implemented")

        class FakeWidget : Widget() {
            override fun createElement() = FakeElement()
        }
    }

    companion object {
        fun root(): BuildContext = BuildContext(element = FakeElement())
        fun child(
            element: Element<*>,
            childs: MutableList<BuildContext> = mutableListOf(),
            parent: BuildContext
        ): BuildContext {
            return BuildContext(
                element = element,
                childs = childs,
                parent = parent,
                listeners = parent.listeners
            )
        }
    }
}

typealias TreeListener = (context: BuildContext, code: String) -> Unit
