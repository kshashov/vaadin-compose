package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.orderedlayout.FlexLayout
import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.widget.StatelessWidget
import io.github.kshashov.vaadincompose.widget.Widget
import kotlin.streams.toList

class ListView<T>(
    private val items: Collection<T>,
    private val render: (item: T) -> Widget,
    private val direction: FlexLayout.FlexDirection = FlexLayout.FlexDirection.COLUMN,
    key: String? = null,
    private val height: String? = null,
    private val width: String? = null,
    private val id: String = "",
    private val classes: Collection<String> = listOf(),
    private val alignItems: String? = null,
    private val justifyContent: String? = null,
    private val postProcess: (FlexLayout.() -> Unit)? = null
) : StatelessWidget(key) {

    override fun build(context: BuildContext): Widget {
        val childs = items.stream()
            .map { item -> render.invoke(item) }
            .toList()

        return Container(
            childs = childs,
            direction = direction,
            height = height,
            width = width,
            id = id,
            classes = classes,
            alignItems = alignItems,
            justifyContent = justifyContent,
            postProcess = postProcess
        )
    }
}
