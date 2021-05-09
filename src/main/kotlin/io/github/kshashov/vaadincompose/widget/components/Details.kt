package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.grid.Grid
import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.widget.StatelessWidget
import io.github.kshashov.vaadincompose.widget.Widget
import kotlin.streams.toList

class Details(
    private val items: Map<String, Widget>,
    key: String? = null,
    val height: String? = null,
    val width: String? = null,
    val id: String = "",
    val classes: Collection<String> = listOf(),
    val alignItems: String? = null,
    val justifyContent: String? = null,
    val postProcess: ((Grid<TableRow>) -> Unit)? = null
) : StatelessWidget(key) {

    override fun build(context: BuildContext): Widget {
        val rows = items.entries.stream()
            .map {
                TableRow(
                    listOf(
                        Label(it.key, postProcess = { it.style.set("font-weight", "bold") }),
                        it.value
                    )
                )
            }
            .toList()

        return Table(
            items = rows,
            key = key,
            columns = listOf(
                ColumnInfo("Property", resizable = true, sortable = false),
                ColumnInfo("Value", resizable = true, sortable = false)
            ),
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
