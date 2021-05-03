package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.shared.Registration
import io.github.kshashov.vaadincompose.widget.EagerChildRenderElement
import io.github.kshashov.vaadincompose.widget.RenderElement
import io.github.kshashov.vaadincompose.widget.RenderWidget
import io.github.kshashov.vaadincompose.widget.Widget
import kotlin.streams.toList

class Table<T>(
    val items: List<T>,
    val columns: List<TableColumn<T>>,
    val onSelection: ((Set<T>) -> Unit)? = null,
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null,
    postProcess: ((Grid<T>) -> Unit)? = null
) : RenderWidget<Grid<T>>(key, height, width, id, classes, alignItems, justifyContent, postProcess) {

    override fun createElement(): GridRenderElement<T> {
        return GridRenderElement(this)
    }

    class GridRenderElement<T>(widget: Table<T>) : EagerChildRenderElement<Table<T>, Grid<T>>(widget) {
        private var reg: Registration? = null
        private var rows: MutableMap<T, TableRow<T>> = HashMap()

        override fun createComponent(): Grid<T> {
            return Grid()
        }

        override fun getChilds(): Collection<Widget> {
            var index = 0

            val columns = widget.columns.stream()
                .filter { it.builder != null }
                .toList()

            rows.clear()
            return widget.items.stream().map {
                val row = TableRow(index++, it, columns)
                rows[it] = row
                return@map row
            }.toList()
        }

        override fun refreshComponent() {
            component.removeAllColumns()

            var builderIndex = 0
            widget.columns.forEach { info ->
                val column: Grid.Column<*>
                if (info.renderer != null) {
                    column = component.addColumn { info.renderer.invoke(it) }
                } else if (info.builder != null) {
                    column = component.addComponentColumn {
                        val rowContext = context.childs[rows[it]!!.index]
                        rowContext.childs[builderIndex++].element.renderedComponent
                    }
                } else {
                    throw IllegalStateException()
                }

                if (info.header != null) {
                    column.setHeader(info.header)
                }
            }

            component.setItems(widget.items)

            reg?.remove()

            val onSelection = widget.onSelection
            if (onSelection != null) {
                reg = component.addSelectionListener {
                    onSelection.invoke(it.allSelectedItems)
                }
            }
        }
    }
}

class TableColumn<T>(
    val header: String? = null,
    val renderer: ((T) -> String)? = null,
    val builder: ((T) -> Widget)? = null
)

class TableRow<T>(
    val index: Int,
    val item: T,
    val columns: List<TableColumn<T>>
) : RenderWidget<RenderElement.EmptyWidget>() {
    override fun createElement() = TableRowElement(this)
}

class TableRowElement<T>(widget: TableRow<T>) :
    EagerChildRenderElement<TableRow<T>, RenderElement.EmptyWidget>(widget) {

    override fun getChilds(): Collection<Widget> {
        return widget.columns.stream()
            .filter { it.builder != null }
            .map { it.builder!!.invoke(widget.item) }
            .toList()
    }

    override fun createComponent() = EMPTY_WIDGET

    override fun refreshComponent() {
        // Do nothing
    }
}
