package io.github.kshashov.vaadincompose.widget

import io.github.kshashov.vaadincompose.BuildContext

abstract class StatefulWidget<STATE : StatefulWidget.WidgetState>(key: String? = null) : Widget(key) {
    override fun createElement(): Element<StatefulWidget<STATE>> {
        return StatefulElement(this)
    }

    abstract fun createState(): STATE

    class StatefulElement<STATE : WidgetState>(widget: StatefulWidget<STATE>) :
        ProxyElement<StatefulWidget<STATE>>(widget) {
        private var widgetState: STATE = widget.createState()

        override fun onAfterRender() {
            super.onAfterRender()
            widgetState.element = this
        }

        override fun getChild(): Widget {
            return widgetState.build(context)
        }

        /**
         * Refreshes element subtree.
         */
        fun rebuild() {
            // Mark all subtree render nodes as dirty
            context.visitNearestElementInheritors(RenderElement::class.java) {
                it.dirty = true
            }

            // Rebuild context subtree
            // Bring back cached nodes or mount new nodes
            updateContextChilds(getChild())

            // The new render nodes are alrady ready to go but we still need to refresh the old dirty ones
            context.visitNearestElementInheritors(RenderElement::class.java) {
                if (it.dirty) {
                    it.refreshComponent()
                    it.dirty = false
                }
            }
        }

        override fun detach() {
            super.detach()
            widgetState.detach();
        }

        override fun dispose() {
            super.dispose()
            widgetState.dispose()
        }
    }

    abstract class WidgetState {
        lateinit var element: StatefulElement<out WidgetState>
        abstract fun build(context: BuildContext): Widget

        protected fun setState(action: () -> Unit) {
            action.invoke()
            element.rebuild()
        }

        /**
         * Invoked when the widget is detached from tree but still have a chance to be reused later.
         */
        open fun detach() {
            // Do nothing
        }

        /**
         * Invoked after the [detach] method if the widget is completely removed from the hierarchy.
         */
        open fun dispose() {
            // Do nothing
        }
    }
}
