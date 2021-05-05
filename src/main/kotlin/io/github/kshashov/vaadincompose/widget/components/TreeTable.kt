package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.treegrid.TreeGrid
import com.vaadin.flow.function.ValueProvider
import io.github.kshashov.vaadincompose.widget.Widget
import java.util.stream.Stream
import kotlin.streams.toList


class TreeTable<T>(
    items: Collection<T>,
    val childsProvider: ValueProvider<T, Collection<T>>,
    columns: Collection<TableColumn<T>>,
    onSelection: ((Set<T>) -> Unit)? = null,
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null,
    postProcess: ((Grid<T>) -> Unit)? = null
) : BaseTable<T, TreeGrid<T>>(
    items,
    columns,
    onSelection,
    key,
    height,
    width,
    id,
    classes,
    alignItems,
    justifyContent,
    postProcess
) {

    override fun createElement(): TreeGridRenderElement<T> {
        return TreeGridRenderElement(this)
    }

    class TreeGridRenderElement<T>(widget: TreeTable<T>) : BaseGridRenderElement<T, TreeGrid<T>, TreeTable<T>>(widget) {
        override fun createComponent(): TreeGrid<T> {
            return TreeGrid()
        }

        override fun addColumn(info: TableColumn<T>, provider: (row: T) -> String): Grid.Column<*> {
            if (info is TreeTableColumn<T>) {
                return component.addHierarchyColumn(provider)
            }

            return super.addColumn(info, provider)
        }

        override fun addComponentColumn(info: TableColumn<T>, provider: (row: T) -> Component): Grid.Column<*> {
            if (info is TreeTableColumn<T>) {
                return component.addComponentHierarchyColumn(provider)
            }

            return super.addComponentColumn(info, provider)
        }

        override fun createActualKeys(items: Collection<T>): Map<T, String> {
            val expanded = flatten(items).toList()
            return super.createActualKeys(expanded)
        }

        override fun setGridItems(items: Collection<T>) {
            component.setItems(widget.items, widget.childsProvider)
        }

        private fun flatten(items: Collection<T>): Stream<T> {
            return Stream.concat(
                items.stream(),
                items.stream().flatMap { flatten(widget.childsProvider.apply(it)) }
            )
        }
    }
}

class TreeTableColumn<T>(
    header: String? = null,
    renderer: ((T) -> String)? = null,
    builder: ((T) -> Widget)? = null,
) : TableColumn<T>(header, renderer, builder) {

}