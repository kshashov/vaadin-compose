package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.shared.Registration
import io.github.kshashov.vaadincompose.widget.*
import java.util.*
import java.util.stream.Collectors
import kotlin.streams.toList

class Table<T>(
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
    postProcess: ((Grid<T>) -> Unit)? = null
) : BaseTable<T, Grid<T>>(
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

    override fun createElement(): GridRenderElement<T> {
        return GridRenderElement(this)
    }

    class GridRenderElement<T>(widget: Table<T>) : BaseGridRenderElement<T, Grid<T>, Table<T>>(widget) {
        override fun createComponent(): Grid<T> {
            return Grid()
        }
    }
}

abstract class BaseTable<T, COMPONENT : Grid<T>>(
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
    postProcess: ((COMPONENT) -> Unit)? = null
) : RenderWidget<COMPONENT>(key, height, width, id, classes, alignItems, justifyContent, postProcess) {
}

abstract class BaseGridRenderElement<T, COMPONENT : Grid<T>, WIDGET : BaseTable<T, COMPONENT>>(widget: WIDGET) :
    ChildSupportRenderElement<WIDGET, COMPONENT>(widget) {
    private var reg: Registration? = null
    private var actualKeys: Map<T, String> = HashMap()

    override fun refreshComponent() {
        super.refreshComponent()

        val items = widget.items

        actualKeys = createActualKeys(items)

        removeObsoleteCache(actualKeys.values.toHashSet())

        // Refresh columns
        component.removeAllColumns()

        val buiderColumns = widget.columns.stream()
            .filter { it.builder != null }
            .toList()

        var builderIndex = 0
        widget.columns.forEach { info ->
            val column: Grid.Column<*>
            if (info.renderer != null) {
                column = addColumn(info) { info.renderer.invoke(it) }
            } else if (info.builder != null) {
                val index = builderIndex++
                column = addComponentColumn(info) {
                    val key = actualKeys[it]!!
                    val rowElement = updateContextChild(key, TableRow(it, buiderColumns))
                    val cellElement = rowElement.context.childs[index].element
                    cellElement.renderedComponent.element.removeFromParent()
                    return@addComponentColumn cellElement.renderedComponent
                }
            } else {
                throw IllegalStateException()
            }

            if (info.header != null) {
                column.setHeader(info.header)
            }
        }

        reg?.remove()

        val onSelection = widget.onSelection
        if (onSelection != null) {
            reg = component.addSelectionListener {
                onSelection.invoke(it.allSelectedItems)
            }
        }

        setGridItems(widget.items)
    }

    protected open fun addColumn(info: TableColumn<T>, provider: (row: T) -> String): Grid.Column<*> {
        return component.addColumn(provider)
    }

    protected open fun addComponentColumn(info: TableColumn<T>, provider: (row: T) -> Component): Grid.Column<*> {
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

open class TableColumn<T>(
    val header: String? = null,
    val renderer: ((T) -> String)? = null,
    val builder: ((T) -> Widget)? = null
)

class TableRow<T>(
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
