package io.github.kshashov.vaadincompose.widget

import io.github.kshashov.vaadincompose.BuildContext

/**
 * Supposed to be used as the base class when the widget doesn't have mutable state and can be freely re-creeated during rendering.
 */
abstract class StatelessWidget(key: String? = null) : Widget(key) {
    override fun createElement(): Element<StatelessWidget> {
        return StatelessElement(this)
    }

    protected abstract fun build(context: BuildContext): Widget

    class StatelessElement(widget: StatelessWidget) : ProxyElement<StatelessWidget>(widget) {

        override fun getChild(): Widget {
            return widget.build(context)
        }
    }
}
