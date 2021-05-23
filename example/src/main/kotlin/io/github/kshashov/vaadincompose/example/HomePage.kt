package io.github.kshashov.vaadincompose.example

import com.vaadin.flow.component.html.Div
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Route

@Route("")
class HomePage : Div(), BeforeEnterObserver {
    override fun beforeEnter(event: BeforeEnterEvent?) {
        event?.forwardTo(CounterPage::class.java)
    }
}
