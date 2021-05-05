package io.github.kshashov.vaadincompose.widget

import io.github.kshashov.vaadincompose.BuildContext

abstract class StatefulWidget(key: String? = null) : Widget(key) {
    override fun createElement(): Element<StatefulWidget> {
        return StatefulElement(this)
    }

    abstract fun createState(): WidgetState<*>

    class StatefulElement(widget: StatefulWidget) : ProxyElement<StatefulWidget>(widget) {
        private var widgetState: WidgetState<*> = widget.createState()
        private var oldWidget: StatefulWidget? = null

        override fun onBeforeWidgetRefresh() {
            super.onBeforeWidgetRefresh()
            oldWidget = widget
        }

        override fun getChild(): Widget {
            return widgetState.build(context)
        }

        override fun init() {
            widgetState.element = this
            widgetState.initState()
        }

        @Suppress("UNCHECKED_CAST")
        override fun didUpdateWidget() {
            widgetState.didUpdateWidget(oldWidget!!)
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
                    it.postProcess()
                    it.dirty = false
                }
            }

            context.notify("rebuild")
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

    abstract class WidgetState<WIDGET : StatefulWidget> {
        internal lateinit var element: StatefulElement
        protected lateinit var widget: WIDGET

        /**
         * Invoked before first state using.
         */
        @Suppress("UNCHECKED_CAST")
        open fun initState() {
            this.widget = element.widget as WIDGET
        }

        /**
         * Invoked after widget instance has been updated.
         */
        @Suppress("UNCHECKED_CAST")
        open fun didUpdateWidget(oldWidget: StatefulWidget) {
            this.widget = element.widget as WIDGET
        }

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
