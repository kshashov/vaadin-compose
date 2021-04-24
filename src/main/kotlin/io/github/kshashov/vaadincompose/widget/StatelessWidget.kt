package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import io.github.kshashov.vaadincompose.BuildContext

abstract class StatelessWidget(key: String? = null) : Widget(key) {
    override fun createElement(): Element<StatelessWidget> {
        return StatelessElement(this)
    }

    protected abstract fun build(context: BuildContext): Widget

    class StatelessElement(widget: StatelessWidget) : Element<StatelessWidget>(widget) {

        override fun render(context: BuildContext): Component {
            val builded = widget.build(context)
            return builded.createElement().render(context)
        }
    }
}
