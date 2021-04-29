package io.github.kshashov.vaadincompose

import com.vaadin.flow.component.HasComponents
import io.github.kshashov.vaadincompose.widget.Widget
import javax.annotation.PostConstruct

interface ComposablePage {

    @PostConstruct
    fun render() {
        if (this !is HasComponents) {
            throw NotImplementedError("ComposablePage is only supportted for HasComponents")
        }

        val context = buildContext()
        val element = build(context).createElement()
        element.mount(context)
        add(element.renderedComponent)
    }

    fun buildContext() = BuildContext.root()

    fun build(context: BuildContext): Widget
}
