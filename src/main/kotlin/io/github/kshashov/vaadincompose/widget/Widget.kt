package io.github.kshashov.vaadincompose.widget

import io.github.kshashov.vaadincompose.BuildContext

abstract class Widget(var key: String? = null) {
    abstract fun createElement(): Element<out Widget>

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun <T : Widget> parentWidget(context: BuildContext, type: Class<T>): T? {
            val widget = context.findNearestElementAncestor {
                type.isAssignableFrom(it.widget.javaClass)
            }?.widget

            return widget as T?
        }
    }
}
