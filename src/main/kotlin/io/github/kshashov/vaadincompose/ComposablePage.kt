package io.github.kshashov.vaadincompose

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.treegrid.TreeGrid
import io.github.kshashov.vaadincompose.widget.Widget
import io.github.kshashov.vaadincompose.widget.components.Label
import io.github.kshashov.vaadincompose.widget.components.SplitLayout
import io.github.kshashov.vaadincompose.widget.components.Table
import io.github.kshashov.vaadincompose.widget.components.TableColumn
import javax.annotation.PostConstruct


interface ComposablePage {

    @PostConstruct
    fun init(): BuildContext {
        if (this !is HasComponents) {
            throw NotImplementedError("ComposablePage is only supported for HasComponents")
        }

        val context = buildContext()

        var grid = Grid<BuildContext>()
        grid.addComponentColumn {
            TextField(it.element.javaClass.toString())
        }

        grid.setItems(listOf(context))


        val element = if (isDebug())
            SplitLayout(
                primary = build(context),
                secondary = debugWidget(context)
            ).createElement()
        else
            build(context).createElement()

        element.mount(context)
        add(element.renderedComponent)

        return context
    }

    fun debugWidget(context: BuildContext): Widget {
        return Table(
            items = listOf(context), columns = listOf(
                TableColumn(renderer = { it.element.state.toString() }, header = "State"),
                TableColumn(builder = { item ->
                    Label(
                        item.element.javaClass.simpleName,
                        postProcess = { it.style.set("font-weight", "bold") })
                }, header = "Element"),
                TableColumn(renderer = { item -> item.element.widget.javaClass.simpleName }, header = "Widget")
            )
        )
    }

    fun isDebug() = false

    fun dispose(context: BuildContext) {
        context.dispose()
    }

    fun buildContext() = BuildContext.root()

    fun build(context: BuildContext): Widget

    fun debugButton(context: BuildContext): Component {
        val grid = TreeGrid<BuildContext>()
        grid.addHierarchyColumn({ "[${it.element.state}] ${it.element.javaClass.simpleName}: ${it.element.widget.javaClass.simpleName}" })

        grid.height = "100%"
        grid.setItems(listOf(context)) { it.childs }
        grid.expandRecursively(listOf(context), 1000)

        val refresh = Button("Refresh") {
            println(context)
            grid.setItems(listOf(context)) { it.childs }
            grid.expandRecursively(listOf(context), 1000)
        }

        val layout = VerticalLayout(refresh, grid)
        layout.height = "100%"

        val dialog = Dialog(layout)
        dialog.width = "50%"
        dialog.height = "80%"
        dialog.isCloseOnOutsideClick = false
        dialog.isCloseOnEsc = true
        dialog.isResizable = true
        dialog.isDraggable = true
        dialog.isModal = false

        val button = Button("Debug") { dialog.open() }
        button.classNames.add("v-debug-button")


        return button
    }
}
