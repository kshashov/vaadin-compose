package io.github.kshashov.vaadincompose

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.dom.Style
import io.github.kshashov.vaadincompose.widget.RenderWidget
import io.github.kshashov.vaadincompose.widget.Widget
import io.github.kshashov.vaadincompose.widget.components.Button
import io.github.kshashov.vaadincompose.widget.components.Conditional
import io.github.kshashov.vaadincompose.widget.components.Container
import io.github.kshashov.vaadincompose.widget.components.Label

interface HasChildWidgets {
    fun add(widget: Widget)
}

class HasSingeChildWidget : HasChildWidgets {
    var child: Widget? = null
        private set

    override fun add(widget: Widget) {
        if (child != null) throw IllegalStateException("Widgets template doesn't allow multiple root widgets")
        child = widget
    }
}

fun ui(block: HasSingeChildWidget.() -> Unit): Widget {
    val hasSingChild = HasSingeChildWidget()
    hasSingChild.block()
    if (hasSingChild.child == null) throw IllegalStateException("Widgets template can't be emty")

    return hasSingChild.child!!
}

fun <COMPONENT : Component> RenderWidget<COMPONENT>.component(postProcess: COMPONENT.() -> Unit) {
    this.postProcess = { it.postProcess() }
}

fun <COMPONENT : Component> RenderWidget<COMPONENT>.style(styleProcess: Style.() -> Unit) {
    this.styleProcess = { it.styleProcess() }
}

fun HasChildWidgets.label(
    text: String = "",
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null,
    styleProcess: ((style: Style) -> Unit)? = null,
    postProcess: ((component: com.vaadin.flow.component.html.Label) -> Unit)? = null
): Label {
    val widget = Label(
        text = text,
        key = key,
        height = height,
        width = width,
        id = id,
        classes = classes,
        alignItems = alignItems,
        justifyContent = justifyContent,
        styleProcess = styleProcess,
        postProcess = postProcess
    )
    this.add(widget)
    return widget
}

fun HasChildWidgets.button(
    text: String? = null,
    onClick: (() -> Unit)? = null,
    icon: Component? = null,
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null,
    styleProcess: ((style: Style) -> Unit)? = null,
    postProcess: ((component: com.vaadin.flow.component.button.Button) -> Unit)? = null
): Button {
    val widget = Button(
        text = text,
        onClick = onClick,
        icon = icon,
        key = key,
        height = height,
        width = width,
        id = id,
        classes = classes,
        alignItems = alignItems,
        justifyContent = justifyContent,
        styleProcess = styleProcess,
        postProcess = postProcess
    )
    this.add(widget)
    return widget
}

fun HasChildWidgets.conditional(
    condition: Boolean,
    primaryBuilder: (() -> Widget),
    secondaryBuilder: (() -> Widget),
    key: String? = null,
): Conditional {
    val widget = Conditional(
        condition = condition,
        primaryBuilder = primaryBuilder,
        secondaryBuilder = secondaryBuilder,
        key = key
    )
    this.add(widget)
    return widget
}

fun HasChildWidgets.container(
    childs: MutableList<Widget> = mutableListOf(),
    direction: FlexLayout.FlexDirection = FlexLayout.FlexDirection.ROW,
    key: String? = null,
    height: String? = null,
    width: String? = null,
    id: String = "",
    classes: Collection<String> = listOf(),
    alignItems: String? = null,
    justifyContent: String? = null,
    styleProcess: ((style: Style) -> Unit)? = null,
    postProcess: ((component: FlexLayout) -> Unit)? = null,
    block: Container.() -> Unit
): Container {
    val widget = Container(
        childs = childs,
        direction = direction,
        key = key,
        height = height,
        width = width,
        id = id,
        classes = classes,
        alignItems = alignItems,
        justifyContent = justifyContent,
        styleProcess = styleProcess,
        postProcess = postProcess
    )
    widget.block()
    this.add(widget)
    return widget
}








