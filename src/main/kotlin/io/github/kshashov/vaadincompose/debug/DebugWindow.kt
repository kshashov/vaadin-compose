package io.github.kshashov.vaadincompose.debug

import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.widget.StatelessWidget
import io.github.kshashov.vaadincompose.widget.Widget
import io.github.kshashov.vaadincompose.widget.components.Provider
import io.github.kshashov.vaadincompose.widget.components.SplitLayout

class DebugWindow(private val child: Widget, private val bloc: DebugToolsBloc, key: String? = null) :
    StatelessWidget(key) {
    override fun build(context: BuildContext): Widget {
        context.addListener { ctx: BuildContext, code: String -> bloc.refresh(ctx, code) }

        return Provider(
            service = bloc,
            child = SplitLayout(
                key = KEY,
                primary = child,
                secondary = DebugWidgetsTree()
            )
        )
    }

    companion object {
        const val KEY = "v-compose-debug-parent"
    }
}
