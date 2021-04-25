package io.github.kshashov.vaadincompose

import io.github.kshashov.vaadincompose.widget.Element

class BuildContext(
        val element: Element<*>? = null,
        val childs: MutableList<BuildContext> = mutableListOf(),
        val parent: BuildContext? = null
) {

    @Suppress("UNCHECKED_CAST")
    fun <T> findNearestElementAncestor(type: Class<T>): T? {
        if (element != null) {
            if (type.isAssignableFrom(element.javaClass)) {
                return element as T
            }
        }
        if (parent == null) {
            return null
        }
        return parent.findNearestElementAncestor(type)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> visitNearestElementInheritors(type: Class<T>, action: (T) -> Unit) {
        if (element != null) {
            if (type.isAssignableFrom(element.javaClass)) {
                action.invoke(element as T)
            }
        }

        for (child in childs) {
            child.visitNearestElementInheritors(type, action)
        }
    }

}
