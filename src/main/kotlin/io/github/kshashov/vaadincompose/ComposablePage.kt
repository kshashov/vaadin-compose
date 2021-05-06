package io.github.kshashov.vaadincompose

import com.vaadin.flow.component.HasComponents
import io.github.kshashov.vaadincompose.debug.DebugToolsBloc
import io.github.kshashov.vaadincompose.debug.DebugWindow
import io.github.kshashov.vaadincompose.widget.Widget
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
            debugWidget(context, bloc, build(context)).createElement()
        } else {
            build(context).createElement()
        }

        element.mount(context)

        add(element.renderedComponent)

        if (isDebug()) {
            bloc.init(
                context.findChildElement {
                    it.widget.key == DebugWindow.KEY
                }?.context?.childs!![0]
            )
            bloc.refresh(context, "init")
        }

        return context
    }

    fun debugWidget(context: BuildContext, bloc: DebugToolsBloc, child: Widget): Widget {
        return DebugWindow(child, bloc)
    }

    fun isDebug() = true

    fun dispose(context: BuildContext) {
        context.dispose()
    }

    fun buildContext() = BuildContext.root()

    fun build(context: BuildContext): Widget
}
