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
            val child = widget.build(context)
            return child.createElement().mount(context)
        }

        override fun updateWidgetContext(widget: Widget) {
            super.updateWidgetContext(widget)
            val child = this.widget.build(context)
            context.childs[0].element?.updateWidgetContext(child)
        }
    }
}
