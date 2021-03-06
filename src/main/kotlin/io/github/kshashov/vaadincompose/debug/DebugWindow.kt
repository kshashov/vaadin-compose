package io.github.kshashov.vaadincompose.debug

import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexLayout
import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.Snapshot
import io.github.kshashov.vaadincompose.widget.StatelessWidget
import io.github.kshashov.vaadincompose.widget.Widget
import io.github.kshashov.vaadincompose.widget.components.*
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation as VaadinOrientation

class DebugWindow(private val childWidget: Widget, key: String? = null) : StatelessWidget(key) {
    override fun build(context: BuildContext): Widget {
        val bloc = Provider.of(DebugToolsBloc::class.java, context)!!
        return Provider(
            service = bloc,
            child = StreamConsumer(
                stream = bloc.horizontal,
                initial = Snapshot.withData(true),
                builder = {
                    SplitLayout(
                        direction = if (it.requireData()) VaadinOrientation.HORIZONTAL else VaadinOrientation.VERTICAL,
                        height = "100%",
                        primary = childWidget,
                        secondary = Container(
                            direction = FlexLayout.FlexDirection.COLUMN,
                            childs = listOf(
                                Container(
                                    direction = FlexLayout.FlexDirection.ROW_REVERSE,
                                    childs = listOf(
                                        Button(
                                            icon = if (it.requireData()) VaadinIcon.SPLIT_V.create() else VaadinIcon.SPLIT_H.create(),
                                            onClick = { bloc.changeOrientation() }) {
                                            addThemeVariants(
                                                ButtonVariant.LUMO_SMALL,
                                                ButtonVariant.LUMO_ICON,
                                                ButtonVariant.LUMO_TERTIARY_INLINE
                                            )
                                        }
                                    )
                                ),
                                DebugPanel(!it.requireData())
                            )
                        )
                    )
                }
            )
        )
    }
}
