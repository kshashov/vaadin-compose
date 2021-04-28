package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.orderedlayout.FlexLayout
import io.github.kshashov.vaadincompose.widget.Element
import io.github.kshashov.vaadincompose.widget.MultiChildRenderElement
import io.github.kshashov.vaadincompose.widget.RenderWidget
import io.github.kshashov.vaadincompose.widget.Widget

/**
 * Reperesents [FlexLayout] component with a single conditional child inside.
 */
class Conditional(
    val condition: Boolean,
    val first: Widget,
    val second: Widget,
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null
) : RenderWidget(key, height, width, id, classes, alignItems, justifyContent) {

    override fun createElement(): Element<Conditional> {
        return ConditionalRenderElement(this)
    }

    /**
     * Swithes child widget according to [Conditional.condition] value.
     * Uses internal cache to get rid of obsolete re-creation of nested elements.
     */
    class ConditionalRenderElement(widget: Conditional) : MultiChildRenderElement<Conditional, FlexLayout>(widget) {
        private var previousCondition: Boolean = widget.condition
        private var trueElement: Element<*>? = null
        private var falseElement: Element<*>? = null

        override fun createComponent(): FlexLayout = FlexLayout()

        override fun onBeforeWidgetRefresh() {
            super.onBeforeWidgetRefresh()

            if (context.childs.isNotEmpty()) {
                // Update internal element cache for previous condition
                previousCondition = this.widget.condition
                putElement(previousCondition, context.childs[0].element)
            }
        }

        override fun updateContextChilds(list: Collection<Widget>) {
            // If condition is changed
            if (widget.condition != previousCondition) {
                // Try to populate high level cache with latest element related to the new codition
                if (containsElement(widget.condition)) {
                    val element = getElement(widget.condition)!!
                    elementsCache.putIfAbsent(key(element.widget, 0), element)
                }
            }

            super.updateContextChilds(list)
        }

        override fun getChilds() = listOf(
            if (this.widget.condition) this.widget.first
            else this.widget.second
        )

        override fun key(widget: Widget, index: Int): String {
            val prefix: String = this.widget.condition.toString()
            return prefix + super.key(widget, index)
        }

        private fun getElement(condition: Boolean) = if (condition) trueElement else falseElement
        private fun containsElement(condition: Boolean) = if (condition) trueElement != null else falseElement != null
        private fun putElement(condition: Boolean, element: Element<*>) {
            if (condition) {
                trueElement = element
            } else {
                falseElement = element
            }
        }
    }
}
