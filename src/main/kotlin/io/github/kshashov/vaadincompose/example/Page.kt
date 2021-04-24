package io.github.kshashov.vaadincompose.example

import com.vaadin.flow.router.Route
import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.ComposablePage
import io.github.kshashov.vaadincompose.widget.Widget
import io.github.kshashov.vaadincompose.widget.components.Container
import io.github.kshashov.vaadincompose.widget.components.Text

@Route("")
class Page : ComposablePage() {

    override fun build(context: BuildContext): Widget {
        return Container(listOf(
                Text("Hello"),
                Text("WORLD!")
        ))
    }
}
