package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import io.github.kshashov.vaadincompose.BuildContext

abstract class StatefulWidget<STATE : StatefulWidget.WidgetState>(key: String? = null) : Widget(key) {
    override fun createElement(): Element<StatefulWidget<STATE>> {
        return StatefulElement(this)
    }

    abstract fun createState(): STATE

    class StatefulElement<STATE : WidgetState>(widget: StatefulWidget<STATE>) : SingleChildElement<StatefulWidget<STATE>>(widget) {
        private var state: STATE = widget.createState()

        override fun render(context: BuildContext): Component {
            val component = super.render(context)
            state.element = this
            return component
        }

        override fun getChild(): Widget {
            return state.build(context)
        }

        fun rebuild() {
            updateContext(widget)

            context.visitNearestElementInheritors(RenderElement::class.java) {
                it.dirty = true
            }

            context.visitNearestElementInheritors(RenderElement::class.java) {
                if (it.dirty) {
                    it.update()
                    it.dirty = false
                }
            }
        }
    }

    abstract class WidgetState {
        lateinit var element: StatefulElement<out WidgetState>
        abstract fun build(context: BuildContext): Widget

        fun setState(action: () -> Unit) {
            action.invoke()
            element.rebuild()
        }
    }
}
