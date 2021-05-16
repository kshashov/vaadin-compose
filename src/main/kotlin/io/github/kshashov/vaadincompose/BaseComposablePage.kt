package io.github.kshashov.vaadincompose

import com.vaadin.flow.component.html.Div
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.BeforeLeaveEvent
import com.vaadin.flow.router.BeforeLeaveObserver

/**
 * The simplest implementation of [ComposablePage] with [Div] component at the root.
 */
abstract class BaseComposablePage : Div(), ComposablePage, BeforeEnterObserver, BeforeLeaveObserver {
    private lateinit var context: BuildContext

    override fun beforeEnter(event: BeforeEnterEvent?) {
        context = init()
    }

    override fun beforeLeave(event: BeforeLeaveEvent?) {
        dispose(context)
    }
}
