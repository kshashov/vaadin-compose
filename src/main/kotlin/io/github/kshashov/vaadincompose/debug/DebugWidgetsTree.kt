package io.github.kshashov.vaadincompose.debug

import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.Snapshot
import io.github.kshashov.vaadincompose.widget.StatelessWidget
import io.github.kshashov.vaadincompose.widget.Widget
import io.github.kshashov.vaadincompose.widget.components.*

class DebugWidgetsTree(key: String? = null) : StatelessWidget(key) {
    override fun build(context: BuildContext): Widget {
        val bloc = Provider.of(DebugToolsBloc::class.java, context)!!

        return StreamConsumer(
            stream = bloc.debugTree,
            initial = Snapshot.empty(),
            builder = { ctx ->
                if (!ctx.hasData) Label("Loading")
                else TreeTable(
                    items = listOf(ctx.requireData()),
                    childsProvider = { it.childs },
                    columns = listOf(
                        TreeTableColumn(
                            builder = { item ->
                                Label(
                                    item.element.javaClass.simpleName,
                                    postProcess = { it.style.set("font-weight", "bold") })
                            }, header = "Element"
                        ),
                        TableColumn(
                            renderer = { item -> item.element.widget.javaClass.simpleName },
                            header = "Widget"
                        ),
                        TableColumn(renderer = { it.element.state.toString() }, header = "State")
                    )
                )
            })
    }
}
