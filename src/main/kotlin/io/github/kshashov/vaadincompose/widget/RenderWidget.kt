package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component
import com.vaadin.flow.dom.Style

abstract class RenderWidget<COMPONENT : Component>(
    key: String? = null,
    val height: String? = null,
    val width: String? = null,
    val id: String = "",
    val classes: Collection<String> = listOf(),
    val alignItems: String? = null,
    val justifyContent: String? = null,
    var styleProcess: ((style: Style) -> Unit)? = null,
    var postProcess: ((component: COMPONENT) -> Unit)? = null
) : Widget(key) {
    abstract override fun createElement(): Element<out RenderWidget<COMPONENT>>
}
