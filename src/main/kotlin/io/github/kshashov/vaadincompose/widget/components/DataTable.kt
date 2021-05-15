package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.shared.Registration
import io.github.kshashov.vaadincompose.widget.*
import java.util.*
import java.util.stream.Collectors

open class DataTable<T>(
    items: Collection<T>,
    columns: Collection<TableColumn<T>>,
    onSelection: ((Set<T>) -> Unit)? = null,
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null,
    postProcess: (Grid<T>.() -> Unit)? = null
) : BaseDataTable<T, Grid<T>>(
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
    override fun createElement(): Element<DataTable<T>> {
        return GridRenderElement(this)
    }

    internal class GridRenderElement<T>(widget: DataTable<T>) :
        BaseGridRenderElement<T, Grid<T>, DataTable<T>>(widget) {
        override fun createComponent(): Grid<T> {
            return Grid()
        }
    }
}

abstract class BaseDataTable<T, COMPONENT : Grid<T>>(
    val items: Collection<T>,
    val columns: Collection<TableColumn<T>>,
    val onSelection: ((Set<T>) -> Unit)? = null,
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null,
    postProcess: (COMPONENT.() -> Unit)? = null
) : RenderWidget<COMPONENT>(key, height, width, id, classes, alignItems, justifyContent, postProcess) {
    internal abstract class BaseGridRenderElement<T, COMPONENT : Grid<T>, WIDGET : BaseDataTable<T, COMPONENT>>(widget: WIDGET) :
        RenderElement<WIDGET, COMPONENT>(widget), HasChildsElement {
        private val cache: MutableMap<String, Element<*>> = HashMap()
        private var actualKeys: Map<T, String> = HashMap()
        private var reg: Registration? = null

        override fun getElementsCache() = cache

        override fun refreshComponent() {
            super.refreshComponent()

            val items = widget.items

            actualKeys = createActualKeys(items)

            removeObsoleteCache(actualKeys.values.toHashSet())

            // Refresh columns
            component.removeAllColumns()

            var index = 0
            widget.columns.forEach { info ->
                val column: Grid.Column<T>
                if (info.renderer != null) {
                    column = addColumn(info) { info.renderer.invoke(it) }
                } else if (info.builder != null) {
                    val builderIndex = index++
                    column = addComponentColumn(info) {
                        val key = actualKeys[it]!!
                        val cellElement =
                            updateContextChild(context, "v-compose-grid$builderIndex$key", info.builder.invoke(it))
                        cellElement.renderedComponent.element.removeFromParent()
                        return@addComponentColumn cellElement.renderedComponent
                    }
                } else {
                    throw IllegalStateException()
                }

                if (info.header != null) {
                    column.setHeader(info.header)
                }

                column.isSortable = info.sortable
                column.isResizable = info.resizable

                info.postProcess?.invoke(column)
            }

            // Refresh selection listener
            reg?.remove()

            val onSelection = widget.onSelection
            if (onSelection != null) {
                reg = component.addSelectionListener {
                    onSelection.invoke(it.allSelectedItems)
                }
            }

            // Refresh items
            val selected = component.selectedItems
            setGridItems(widget.items)
            // Restore selection
            selected.forEach { component.select(it) }
        }

        protected open fun addColumn(info: TableColumn<T>, provider: (row: T) -> String): Grid.Column<T> {
            return component.addColumn(provider)
        }

        protected open fun addComponentColumn(info: TableColumn<T>, provider: (row: T) -> Component): Grid.Column<T> {
            return component.addComponentColumn(provider)
        }

        protected open fun createActualKeys(items: Collection<T>): Map<T, String> {
            return items.stream()
                .collect(Collectors.toMap({ it }, {
                    if (actualKeys.containsKey(it)) actualKeys[it]!!
                    else "v-compose-grid-" + UUID.randomUUID().toString()
                }))
        }

        protected open fun setGridItems(items: Collection<T>) {
            component.setItems(items)
        }
    }
}

open class ColumnInfo<T>(
    val header: String? = null,
    val sortable: Boolean = true,
    val resizable: Boolean = true
)

open class TableColumn<T>(
    header: String? = null,
    sortable: Boolean = true,
    resizable: Boolean = true,
    val renderer: ((T) -> String)? = null,
    val builder: ((T) -> Widget)? = null,
    val postProcess: ((Grid.Column<T>) -> Unit)? = null
) : ColumnInfo<T>(header, sortable, resizable)
