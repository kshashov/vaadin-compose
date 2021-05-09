package io.github.kshashov.vaadincompose

import com.vaadin.flow.component.HasComponents
import io.github.kshashov.vaadincompose.debug.DebugToolsBloc
import io.github.kshashov.vaadincompose.debug.DebugWindow
import io.github.kshashov.vaadincompose.widget.Widget
import io.github.kshashov.vaadincompose.widget.components.Provider
import javax.annotation.PostConstruct


interface ComposablePage {

    @PostConstruct
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

    fun debugWidget(context: BuildContext, bloc: DebugToolsBloc, child: Widget): Widget {
        return DebugWindow(child)
    }

    fun isDebug() = true

    fun dispose(context: BuildContext) {
        context.element.dispose()
    }

    fun buildContext() = BuildContext.root()

    fun build(context: BuildContext): Widget
}
