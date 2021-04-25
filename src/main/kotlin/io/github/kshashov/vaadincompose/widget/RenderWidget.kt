package io.github.kshashov.vaadincompose.widget

abstract class RenderWidget(
        key: String? = null,
        val height: String? = null,
        val width: String? = null,
        val id: String = "",
        val classes: Collection<String> = listOf(),
        val alignItems: String? = null,
        val justifyContent: String? = null,
) : Widget(key) {
    abstract override fun createElement(): Element<out RenderWidget>
}
