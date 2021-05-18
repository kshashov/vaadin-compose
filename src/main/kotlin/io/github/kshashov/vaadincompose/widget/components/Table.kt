package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.grid.Grid
import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.widget.StatelessWidget
import io.github.kshashov.vaadincompose.widget.Widget
import java.util.*

class Table(
    private val items: List<TableRow>,
    private val columns: Collection<ColumnInfo<TableRow>> = listOf(),
    key: String? = null,
    private val height: String? = null,
    private val width: String? = null,
    private val id: String = "",
    private val classes: Collection<String> = listOf(),
    private val alignItems: String? = null,
    private val justifyContent: String? = null,
    private val postProcess: (Grid<TableRow>.() -> Unit)? = null
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

class TableRow(val items: List<Widget>)
