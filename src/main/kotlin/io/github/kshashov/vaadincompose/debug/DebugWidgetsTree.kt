package io.github.kshashov.vaadincompose.debug

import com.vaadin.flow.component.orderedlayout.FlexLayout
import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.Snapshot
import io.github.kshashov.vaadincompose.widget.HasDebugInfo
import io.github.kshashov.vaadincompose.widget.StatelessWidget
import io.github.kshashov.vaadincompose.widget.Widget
import io.github.kshashov.vaadincompose.widget.components.*
import java.util.*

class DebugPanel(private val horizontal: Boolean, key: String? = null) : StatelessWidget(key) {
    override fun build(context: BuildContext): Widget {
        return SplitLayout(
            height = "100%",
            width = "100%",
            primary = DebugTree(),
            secondary = DebugDetail(),
            orientation = if (horizontal) com.vaadin.flow.component.splitlayout.SplitLayout.Orientation.HORIZONTAL else com.vaadin.flow.component.splitlayout.SplitLayout.Orientation.VERTICAL,
        )
    }
}

class DebugTree(key: String? = null) : StatelessWidget(key) {
    override fun build(context: BuildContext): Widget {
        val bloc = Provider.of(DebugToolsBloc::class.java, context)!!

        return StreamConsumer(
            stream = bloc.debugTree,
            initial = Snapshot.empty(),
            builder = { ctx ->
                if (!ctx.hasData) Label("Loading")
                else TreeDataTable(
                    height = "100%",
                    items = listOf(ctx.requireData()),
                    childsProvider = { it.childs },
                    onSelection = {
                        if (it.isNotEmpty()) bloc.select(it.firstOrNull())
                    },
                    columns = listOf(
                        TreeTableColumn(
                            builder = { item: BuildContext ->
                                val element = item.element
                                val widget = element.widget

                                var caption = widget.javaClass.simpleName
                                if (widget.key != null) caption = "$caption (key: ${widget.key})"

                                return@TreeTableColumn Container(
                                    direction = FlexLayout.FlexDirection.ROW,
                                    childs = listOf(
                                        Label(caption, postProcess = {
                                            it.style
                                                .set("font-weight", "bold")
                                                .set("margin", "0 10px")
                                        }),
                                        Label(element.javaClass.simpleName, postProcess = {
                                            it.style.set("color", "gray")
                                        }),
                                    ),
                                )
                            },
                            postProcess = {
                                it.setFlexGrow(2)
                            }
                        )
                    ),
                    postProcess = {
                        it.expandRecursively(listOf(ctx.requireData()), 10000)
                        it.minHeight = "300px"
                        it.minWidth = "300px"
                    }
                )
            })
    }

}

class DebugDetail(key: String? = null) : StatelessWidget(key) {
    override fun build(context: BuildContext): Widget {
        val bloc = Provider.of(DebugToolsBloc::class.java, context)!!

        return StreamConsumer(
            stream = bloc.selected,
            initial = Snapshot.empty(),
            builder = { selected ->
                Conditional(
                    condition = !selected.hasData,
                    primaryBuilder = { Label("") },
                    secondaryBuilder = {
                        val ctx = selected.requireData()
                        val el = ctx.element
                        val wd = el.widget

                        val data = linkedMapOf(
                            Pair("Widget", typeCell(wd)),
                            Pair("Key", Label(Objects.toString(wd.key))),
                            Pair("Element", typeCell(el)),
                            Pair("Component", typeCell(el.renderedComponent)),
                        )

                        if (el is HasDebugInfo && (el.getDebugInfo() != null)) {
                            data.putAll(el.getDebugInfo()!!.mapValues {
                                val detail = if (it.value is String)
                                    Label(it.value as String)
                                else
                                    typeCell(it.value)
                                return@mapValues detail
                            })
                        }

                        return@Conditional Details(
                            data,
                            width = "100%",
                            height = "100%",
                            postProcess = {
                                it.minHeight = "300px"
                                it.minWidth = "300px"
                            }
                        )
                    }

                )
            })
    }

    private fun typeCell(obj: Any): Widget {
        return Label(obj(obj), postProcess = {
            it.element.setAttribute("title", obj(obj, false))
        })
    }

    private fun obj(obj: Any, short: Boolean = true): String {
        val javaClass = obj.javaClass
        return (if (short) javaClass.simpleName else javaClass.name) + "@" + Integer.toHexString(obj.hashCode())
    }

}
