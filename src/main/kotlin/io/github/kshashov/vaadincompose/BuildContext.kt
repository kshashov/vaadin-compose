package io.github.kshashov.vaadincompose

import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.Widget

class BuildContext(
    val element: Element<*>,
    val childs: MutableList<BuildContext> = mutableListOf(),
    val parent: BuildContext? = null
) {
    @Suppress("UNCHECKED_CAST")
    fun <T : Element<*>> findNearestElementAncestor(type: Class<T>): T? {
        return findNearestElementAncestor {
            type.isAssignableFrom(it.javaClass)
        } as T?
    }

    fun findNearestElementAncestor(filter: (element: Element<*>) -> Boolean): Element<*>? {
        if (filter.invoke(element)) {
            return element
        }

        return parent?.findNearestElementAncestor(filter)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> visitNearestElementInheritors(type: Class<T>, action: (T) -> Unit) {
        if (type.isAssignableFrom(element.javaClass)) {
            action.invoke(element as T)
        }

        for (child in childs) {
            child.visitNearestElementInheritors(type, action)
        }
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
    }
}
