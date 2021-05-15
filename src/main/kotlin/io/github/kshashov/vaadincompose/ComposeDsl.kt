package io.github.kshashov.vaadincompose

import com.vaadin.flow.component.Component
import com.vaadin.flow.dom.Element
import com.vaadin.flow.dom.Style

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
annotation class VComposeDsl

fun (@VComposeDsl Component).style(styleProcess: (@VComposeDsl Style).() -> Unit) {
    this.element.style.styleProcess()
}

fun (@VComposeDsl Component).element(elementProcess: (@VComposeDsl Element).() -> Unit) {
    this.element.elementProcess()
}








