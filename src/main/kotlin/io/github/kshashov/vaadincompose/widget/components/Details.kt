package io.github.kshashov.vaadincompose.widget.components

import com.vaadin.flow.component.grid.Grid
import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.style
import io.github.kshashov.vaadincompose.widget.StatelessWidget
import io.github.kshashov.vaadincompose.widget.Widget
import java.util.stream.Collectors

class Details(
    private val items: Map<String, Widget>,
    key: String? = null,
    private val height: String? = null,
    private val width: String? = null,
    private val id: String = "",
    private val classes: Collection<String> = listOf(),
    private val alignItems: String? = null,
    private val justifyContent: String? = null,
    private val postProcess: (Grid<TableRow>.() -> Unit)? = null
) : StatelessWidget(key) {

    override fun build(context: BuildContext): Widget {
        val rows = items.entries.stream()
            .map {
                TableRow(
                    listOf(
                        Label(it.key) {
                            style {
                                set("font-weight", "bold")
                            }
                        },
                        it.value
                    )
                )
            }
            .collect(Collectors.toList())

        val details = this

        return Table(
            items = rows,
            columns = listOf(
                ColumnInfo("Property", resizable = true, sortable = false),
                ColumnInfo("Value", resizable = true, sortable = false)
            ),
            key = key,
            height = details.height,
            width = details.width,
            id = details.id,
            classes = details.classes,
            alignItems = details.alignItems,
            justifyContent = details.justifyContent,
            postProcess = details.postProcess
        )
    }
}
