package io.github.kshashov.vaadincompose

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.treegrid.TreeGrid
import io.github.kshashov.vaadincompose.widget.Widget
import io.github.kshashov.vaadincompose.widget.components.Label
import io.github.kshashov.vaadincompose.widget.components.SplitLayout
import javax.annotation.PostConstruct


interface ComposablePage {

    @PostConstruct
    fun init(): BuildContext {
        if (this !is HasComponents) {
            throw NotImplementedError("ComposablePage is only supported for HasComponents")
        }

        val context = buildContext()


        val element = if (isDebug())
            SplitLayout(
                primary = build(context),
                secondary = debugWidget()
            ).createElement()
        else
            build(context).createElement()

        element.mount(context)
        add(element.renderedComponent)

        return context
    }

    fun debugWidget(): Widget {
        return Label("TODO")
    }

    fun isDebug() = true

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
