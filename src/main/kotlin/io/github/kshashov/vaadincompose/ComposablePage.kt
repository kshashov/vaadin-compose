package io.github.kshashov.vaadincompose

import com.vaadin.flow.component.HasComponents
import io.github.kshashov.vaadincompose.debug.DebugToolsBloc
import io.github.kshashov.vaadincompose.debug.DebugWindow
import io.github.kshashov.vaadincompose.widget.Widget
import io.github.kshashov.vaadincompose.widget.components.Provider

/**
 * Should be used to add Compose support to Vaadin routes.
 */
interface ComposablePage {

    /**
     * Should be invoked when the page is ready to add a rendered Vaadin Component.
     */
    fun init(): BuildContext {
        if (this !is HasComponents) {
            throw NotImplementedError("ComposablePage is only supported for HasComponents")
        }

        val bloc = DebugToolsBloc()
        val context = buildContext()

        val element = if (isDebug()) {
            Provider(
                service = bloc,
                child = debugWidget(context, bloc, build(context))
            ).createElement()
        } else {
            build(context).createElement()
        }

        element.mount(context)

        add(element.renderedComponent)

        if (isDebug()) {
            bloc.init(context)
        }

        return context
    }

    /**
     * Should be used to trigger the same method invocations in the nested Elements and Widget States before the Page is closed.
     */
    fun dispose(context: BuildContext) {
        context.element.dispose()
    }

    fun debugWidget(context: BuildContext, bloc: DebugToolsBloc, child: Widget): Widget {
        return DebugWindow(child)
    }

    fun isDebug() = false

    fun buildContext() = BuildContext.root()

    fun build(context: BuildContext): Widget
}
