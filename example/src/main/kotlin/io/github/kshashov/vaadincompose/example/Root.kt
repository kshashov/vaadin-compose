package io.github.kshashov.vaadincompose.example

import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.RouterLayout
import com.vaadin.flow.router.RouterLink

@CssImport("./styles/styles.css")
class Root : HorizontalLayout(), RouterLayout {
    private val menu = VerticalLayout()
    private val content = VerticalLayout()
    private val description = Span()

    init {
        setSizeFull()
        menu.setWidth(null)
        content.setSizeFull()
        description.setWidthFull()
        description.addClassName("description")

        with(menu) {
            add(RouterLink("Counter", CounterPage::class.java))
            add(RouterLink("Conditional", ConditionalPage::class.java))
            add(RouterLink("Dialog", DialogPage::class.java))
            add(RouterLink("Todo list", TodoPage::class.java))
        }

        content.add(description)
        add(menu)
        add(content)
    }

    override fun showRouterLayoutContent(content: HasElement?) {
        if (content == null) return

        val text = content.javaClass.getAnnotation(Description::class.java)
        description.element.setProperty("innerHTML", text?.value ?: "")

        with(content.element.style) {
            set("width", "100%")
            set("height", "100%")
        }

        this.content.element.appendChild(content.element)
    }
}
