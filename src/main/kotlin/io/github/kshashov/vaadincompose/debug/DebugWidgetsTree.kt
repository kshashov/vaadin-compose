package io.github.kshashov.vaadincompose.debug

import com.vaadin.flow.component.orderedlayout.FlexLayout
import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.Snapshot
import io.github.kshashov.vaadincompose.widget.*
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

private class DebugTree(key: String? = null) : StatelessWidget(key) {
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
                            builder = { debugTreeColumnBuilder(it.element) },
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

private class DebugDetail(key: String? = null) : StatelessWidget(key) {
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
                            "Widget" to typeCell(wd),
                            "Key" to Label(Objects.toString(wd.key)),
                            "Element" to typeCell(el),
                            "Component" to typeCell(el.renderedComponent),
                            "State" to Label(el.state.toString())
                        )

                        if (el is HasDebugInfoElement && (el.getDebugInfo() != null)) {
                            data.putAll(el.getDebugInfo()!!.mapValues {
                                val detail = when (it.value) {
                                    is String -> Label(it.value as String)
                                    else -> typeCell(it.value)
                                }
                                return@mapValues detail
                            })
                        }

                        if (el is HasDebugCacheInfoElement && (el.getDebugCacheInfo() != null)) {
                            data["Cache"] = DebugCacheCell(el.getDebugCacheInfo()!!)
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


}

private class DebugCacheCell(private val cache: Map<String, Element<*>>, key: String? = null) : StatelessWidget(key) {
    private val reversed: Map<Element<*>, String> = cache.entries.associateBy({ it.value }) { it.key }

    override fun build(context: BuildContext): Widget {
        return TreeDataTable(
            height = "100%",
            width = "100%",
            items = cache.values,
            childsProvider = { it.context.childs.map { it.element } },
            columns = listOf(
                TreeTableColumn(
                    builder = {
                        var cell = debugTreeColumnBuilder(it)
                        if (reversed.contains(it)) {
                            val key = reversed[it]!!
                            val label = Label(key, postProcess = {
                                it.style.set("font-size", "smaller")
                                it.element.setAttribute("title", key)
                            })
                            cell = Container(
                                mutableListOf(cell, label),
                                direction = FlexLayout.FlexDirection.COLUMN,
                                width = "100%"
                            )
                        }
                        return@TreeTableColumn cell
                    },
                    postProcess = {
                        it.setFlexGrow(2)
                    }
                )
            ),
            postProcess = {
                it.minHeight = "200px"
                it.minWidth = "200px"
            }
        )

    }
}

private fun debugTreeColumnBuilder(element: Element<*>): Widget {
    val widget = element.widget

    var caption = widget.javaClass.simpleName
    if (widget.key != null) caption = "$caption (key: ${widget.key})"

    return Container(
        direction = FlexLayout.FlexDirection.ROW,
        childs = mutableListOf(
            Label(caption, postProcess = {
                it.style
                    .set("font-weight", "bold")
                    .set("margin-right", "10px")
                it.element.setAttribute("title", obj(widget, false))
            }),
            Label(element.javaClass.simpleName, postProcess = {
                it.style.set("color", "gray")
                it.element.setAttribute("title", obj(element, false))
            }),
        ),
    )
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


