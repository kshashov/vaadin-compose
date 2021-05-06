package io.github.kshashov.vaadincompose.debug

import com.vaadin.flow.component.orderedlayout.FlexLayout
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
                    height = "100%",
                    items = listOf(ctx.requireData()),
                    childsProvider = { it.childs },
                    columns = listOf(
                        TreeTableColumn(
                            builder = { item ->
                                Container(
                                    direction = FlexLayout.FlexDirection.ROW,
                                    childs = listOf(
                                        Label(item.element.widget.javaClass.simpleName, postProcess = {
                                            it.style
                                                .set("font-weight", "bold")
                                                .set("margin", "0 10px")
                                        }),
                                        Label(item.element.javaClass.simpleName)
                                    ),
                                )
                            }, header = "Element",
                            postProcess = {
                                it.setFlexGrow(2)
                            }
                        )
                    ),
                    postProcess = {
                        it.expandRecursively(listOf(ctx.requireData()), 10000)
                        it.setMinHeight("500px")
                    }
                )
            })
    }
}
