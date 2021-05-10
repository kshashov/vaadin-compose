package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.dom.Style
import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.HasChildWidgets
import io.github.kshashov.vaadincompose.widget.StatelessWidget
import io.github.kshashov.vaadincompose.widget.Widget
import java.util.*

class Table(
    private val items: Collection<TableRow>,
    private val columns: Collection<ColumnInfo<TableRow>> = listOf(),
    key: String? = null,
    val height: String? = null,
    val width: String? = null,
    val id: String = "",
    val classes: Collection<String> = listOf(),
    val alignItems: String? = null,
    val justifyContent: String? = null,
    val styleProcess: ((style: Style) -> Unit)? = null,
    val postProcess: ((Grid<TableRow>) -> Unit)? = null
) : StatelessWidget(key) {

    override fun build(context: BuildContext): Widget {
        val columnsCount = Collections.max(items, Comparator.comparing { it.items.size }).items.size
        val tableColumns = mutableListOf<TableColumn<TableRow>>()
        for (i in 0 until columnsCount) {
            val column = if (i < columns.size) {
                val info = columns.elementAt(i)
                TableColumn<TableRow>(
                    builder = { row -> row.items[i] },
                    header = info.header,
                    resizable = info.resizable,
                    sortable = info.sortable
                )
            } else {
                TableColumn<TableRow>(builder = { row -> row.items[i] })
            }

            tableColumns.add(column)
        }

        return DataTable(
            items = items,
            columns = tableColumns,
            onSelection = null,
            key = key,
            height = height,
            width = width,
            id = id,
            classes = classes,
            alignItems = alignItems,
            justifyContent = justifyContent,
            styleProcess = styleProcess,
            postProcess = postProcess
        )
    }
}


class TableRow(val items: MutableList<Widget>) : HasChildWidgets {
    override fun add(widget: Widget) {
        items.add(widget)
    }
}
