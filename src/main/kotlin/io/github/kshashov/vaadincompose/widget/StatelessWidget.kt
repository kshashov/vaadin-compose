package io.github.kshashov.vaadincompose.widget

import io.github.kshashov.vaadincompose.BuildContext

abstract class StatelessWidget(key: String? = null) : Widget(key) {
    override fun createElement(): Element<StatelessWidget> {
        return StatelessElement(this)
    }

    protected abstract fun build(context: BuildContext): Widget

    class StatelessElement(widget: StatelessWidget) : SingleChildElement<StatelessWidget>(widget) {

        override fun getChild(): Widget {
            return widget.build(context)
        }
    }
}
