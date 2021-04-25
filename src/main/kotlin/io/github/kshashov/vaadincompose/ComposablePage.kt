package io.github.kshashov.vaadincompose

import com.vaadin.flow.component.html.Div
import io.github.kshashov.vaadincompose.widget.Widget
import io.github.kshashov.vaadincompose.widget.components.Container
import javax.annotation.PostConstruct

abstract class ComposablePage : Div() {

    @PostConstruct
    fun render() {
        val context = buildContext()
        val container = Container(listOf(build(context)))
        add(container.createElement().mount(context))
    }

    protected fun buildContext() = BuildContext()

    abstract fun build(context: BuildContext): Widget
}
