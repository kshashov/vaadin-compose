package io.github.kshashov.vaadincompose.widget.components

import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.widget.StatelessWidget
import io.github.kshashov.vaadincompose.widget.Widget

class Provider<T : Any>(val child: Widget, val service: T, key: String? = null) : StatelessWidget(key) {
    override fun build(context: BuildContext): Widget {
        return child
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun <T : Any> of(type: Class<T>, context: BuildContext): T? {
            val widget = context.findParentElement {
                val saved = it.widget
                (saved is Provider<*>) && (type.isAssignableFrom(saved.service.javaClass))
            }?.widget

            return (widget as Provider<T>?)?.service
        }
    }
}
