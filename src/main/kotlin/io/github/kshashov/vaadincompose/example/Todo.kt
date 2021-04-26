package io.github.kshashov.vaadincompose.example

import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.router.Route
import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.ComposablePage
import io.github.kshashov.vaadincompose.widget.StatefulWidget
import io.github.kshashov.vaadincompose.widget.Widget
import io.github.kshashov.vaadincompose.widget.components.*

@CssImport("./styles/styles.css")
@Route("")
class Todo : Div(), ComposablePage {

    override fun build(context: BuildContext): Widget {
        return MainWidget()
    }

    class MainWidget : StatefulWidget<MainState>() {
        override fun createState() = MainState()
    }

    class MainState : StatefulWidget.WidgetState() {
        private var items: MutableList<TodoItem> = mutableListOf()
        private var counter: Int = 0;
        private var text: String = ""

        override fun build(context: BuildContext): Widget {
            return Container(
                    height = "100%",
                    direction = FlexLayout.FlexDirection.COLUMN,
                    classes = listOf("main-widget"),
                    components = listOf(
                            Container(
                                    alignItems = "baseline",
                                    direction = FlexLayout.FlexDirection.ROW,
                                    components = listOf(
                                            Text(label = "New Item", text = this.text,
                                                    onChanged = { this.text = it }),
                                            Button("Add", {
                                                setState {
                                                    items.add(TodoItem(counter++, text))
                                                    text = ""
                                                }
                                            }))),
                            ListView(
                                    height = "100%",
                                    direction = FlexLayout.FlexDirection.COLUMN_REVERSE,
                                    items = this.items,
                                    render = {
                                        TodoItemWidget(
                                                key = it.id.toString(),
                                                item = it,
                                                delete = { setState { items.remove(it) } },
                                                down = { setState { down(it) } },
                                                up = { setState { up(it) } })
                                    }
                            )))

        }

        private fun up(it: TodoItem) {
            val ind = items.indexOf(it)
            if (ind == (items.size - 1)) return
            items.add(ind + 1, items.removeAt(ind))
        }

        private fun down(it: TodoItem) {
            val ind = items.indexOf(it)
            if (ind == 0) return
            items.add(ind - 1, items.removeAt(ind))
        }
    }

    class TodoItem(var id: Int, var text: String)

    class TodoItemWidget(
            key: String? = null,
            val item: TodoItem,
            val delete: (item: TodoItem) -> Unit,
            val up: (item: TodoItem) -> Unit,
            val down: (item: TodoItem) -> Unit) : StatefulWidget<TodoItemWidgetState>(key) {
        override fun createState(): TodoItemWidgetState = TodoItemWidgetState(item, delete, up, down)

    }

    class TodoItemWidgetState(
            val item: TodoItem,
            val delete: (item: TodoItem) -> Unit,
            val up: (item: TodoItem) -> Unit,
            val down: (item: TodoItem) -> Unit) : StatefulWidget.WidgetState() {
        override fun build(context: BuildContext): Widget {
            return Container(
                    alignItems = "center",
                    direction = FlexLayout.FlexDirection.ROW,
                    classes = listOf("card", "todo-item"),
                    components = listOf(
                            Button("x", { delete.invoke(item) }),
                            Button("\uD83E\uDC17", { down.invoke(item) }),
                            Button("\uD83E\uDC15", { up.invoke(item) }),
                            Label(item.text)))
        }

    }
}




