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
        val container = build(context)

        add(container.createElement().mount(context))
    }

    fun buildContext() = BuildContext()

    fun build(context: BuildContext): Widget
}
