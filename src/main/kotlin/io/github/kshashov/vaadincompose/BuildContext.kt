package io.github.kshashov.vaadincompose

import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.Widget

class BuildContext(
        val element: Element<*> = FakeElement(),
        val childs: MutableList<BuildContext> = mutableListOf(),
        val parent: BuildContext? = null
) {

    @Suppress("UNCHECKED_CAST")
    fun <T> findNearestElementAncestor(type: Class<T>): T? {
        if (type.isAssignableFrom(element.javaClass)) {
            return element as T
        }

        if (parent == null) {
            return null
        }
        return parent.findNearestElementAncestor(type)
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


    class FakeElement() : Element<FakeElement.FakeWidget>(FakeWidget()) {
        override fun render(context: BuildContext) = TODO("Not yet implemented")

        class FakeWidget : Widget() {
            override fun createElement() = FakeElement()
        }
    }

}
