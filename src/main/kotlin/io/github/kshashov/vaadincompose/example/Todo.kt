package io.github.kshashov.vaadincompose.example

import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.router.Route
import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.ComposablePage
import io.github.kshashov.vaadincompose.Snapshot
import io.github.kshashov.vaadincompose.widget.StatefulWidget
import io.github.kshashov.vaadincompose.widget.StatelessWidget
import io.github.kshashov.vaadincompose.widget.Widget
import io.github.kshashov.vaadincompose.widget.components.*
import io.reactivex.rxjava3.subjects.BehaviorSubject

@CssImport("./styles/styles.css")
@Route("")
class Todo : Div(), ComposablePage {

    override fun build(context: BuildContext): Widget {
        return Provider(child = MainWidget(), service = TodoBloc(ArrayList()))
    }

    class MainWidget(key: String? = null) : StatelessWidget(key) {
        private var text: String = ""

        override fun build(context: BuildContext): Widget {
            val bloc: TodoBloc = Provider.of(TodoBloc::class.java, context)!!

            return Container(
                height = "100%",
                direction = FlexLayout.FlexDirection.COLUMN,
                classes = listOf("main-widget"),
                childs = listOf(
                    Container(
                        alignItems = "baseline",
                        direction = FlexLayout.FlexDirection.ROW,
                        childs = listOf(
                            StreamConsumer(
                                initial = Snapshot.withData(""),
                                stream = bloc.texts,
                                builder = { text ->
                                    this.text = text.requireData()
                                    return@StreamConsumer Text(
                                        label = "New Item",
                                        text = text.requireData(),
                                        onChanged = { this.text = it })
                                }),
                            Button("Add", {
                                bloc.add(text)
                            })
                        )
                    ),
                    StreamConsumer(
                        initial = Snapshot.withData(mutableListOf()),
                        stream = bloc.todos,
                        builder = { items ->
                            ListView(
                                height = "100%",
                                direction = FlexLayout.FlexDirection.COLUMN_REVERSE,
                                items = items.requireData(),
                                render = { TodoItemWidget(item = it, key = it.id.toString()) }
                            )
                        })
                )
            )

        }
    }

    class TodoItem(var id: Int, var text: String)

    class TodoItemWidget(val item: TodoItem, key: String? = null) : StatefulWidget(key) {
        override fun createState(): TodoItemWidgetState = TodoItemWidgetState()

    }

    class TodoItemWidgetState : StatefulWidget.WidgetState<TodoItemWidget>() {

        override fun build(context: BuildContext): Widget {
            val bloc: TodoBloc = Provider.of(TodoBloc::class.java, context)!!

            return Container(
                alignItems = "center",
                direction = FlexLayout.FlexDirection.ROW,
                classes = listOf("card", "todo-item"),
                childs = listOf(
                    Button("x", { bloc.remove(widget.item) }),
                    Button("\uD83E\uDC17", { bloc.down(widget.item) }),
                    Button("\uD83E\uDC15", { bloc.up(widget.item) }),
                    Label(widget.item.text)
                )
            )
        }

        override fun detach() {
            super.detach()
        }

        override fun dispose() {
            super.dispose()
        }
    }

    class TodoBloc(private val items: MutableList<TodoItem>) {
        private var counter: Int = 0
        val todos: BehaviorSubject<MutableList<TodoItem>> = BehaviorSubject.createDefault(items)
        val texts: BehaviorSubject<String> = BehaviorSubject.createDefault("")

        fun add(text: String) {
            items.add(TodoItem(id = counter++, text))
            this.texts.onNext("")
            todos.onNext(items)
        }

        fun up(it: TodoItem) {
            val ind = items.indexOf(it)
            if (ind == (items.size - 1)) return
            items.add(ind + 1, items.removeAt(ind))
            todos.onNext(items)
        }

        fun down(it: TodoItem) {
            val ind = items.indexOf(it)
            if (ind == 0) return
            items.add(ind - 1, items.removeAt(ind))
            todos.onNext(items)
        }

        fun remove(it: TodoItem) {
            items.remove(it)
            todos.onNext(items)
        }
    }
}




