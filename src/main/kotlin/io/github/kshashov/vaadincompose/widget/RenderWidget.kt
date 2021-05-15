package io.github.kshashov.vaadincompose.widget

import com.vaadin.flow.component.Component

abstract class RenderWidget<COMPONENT : Component>(
    key: String? = null,
    val height: String? = null,
    val width: String? = null,
    val id: String = "",
    val classes: Collection<String> = listOf(),
    val alignItems: String? = null,
    val justifyContent: String? = null,
    val postProcess: (COMPONENT.() -> Unit)? = null
) : Widget(key) {
    abstract override fun createElement(): Element<out RenderWidget<COMPONENT>>
}
