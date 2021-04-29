package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.orderedlayout.FlexLayout
import io.github.kshashov.vaadincompose.utils.BooleanPairHolder
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.ProxyElement
import io.github.kshashov.vaadincompose.widget.Widget

/**
 * Reperesents [FlexLayout] component with a single conditional child inside.
 */
class Conditional(
    val condition: Boolean,
    val first: Widget,
    val second: Widget,
    key: String? = null,
) : Widget(key) {

    override fun createElement(): Element<Conditional> {
        return ConditionalRenderElement(this)
    }

    /**
     * Swithes child widget according to [Conditional.condition] value.
     * Uses internal cache to get rid of obsolete re-creation of nested elements.
     */
    class ConditionalRenderElement(widget: Conditional) : ProxyElement<Conditional>(widget) {
        private var previousCondition: Boolean = widget.condition
        private var pairHolder: BooleanPairHolder<Element<*>> = BooleanPairHolder()

        /**
         * Saves previous element to [pairHolder] cache.
         */
        override fun onBeforeWidgetRefresh() {
            super.onBeforeWidgetRefresh()

            if (context.childs.isNotEmpty()) {
                // Update internal element cache for previous condition
                previousCondition = this.widget.condition
                pairHolder.putElement(previousCondition, context.childs[0].element)
            }
        }

        /**
         * Restores element from [pairHolder] cache and puts it into outer [elementsCache] cache.
         */
        override fun updateContextChilds(child: Widget) {
            // If condition is changed
            if (widget.condition != previousCondition) {
                // Try to populate high level cache with latest element related to the new codition
                if (pairHolder.containsElement(widget.condition)) {
                    val element = pairHolder.getElement(widget.condition)!!
                    elementsCache.putIfAbsent(key(element.widget, 0), element)
                }
            }

            super.updateContextChilds(child)
        }

        override fun getChild() = if (this.widget.condition) this.widget.first else this.widget.second

        /**
         * Overrides key to have an ability to store both elements even for the same widget class.
         */
        override fun key(widget: Widget, index: Int): String {
            val prefix: String = this.widget.condition.toString()
            return prefix + super.key(widget, index)
        }
    }
}
