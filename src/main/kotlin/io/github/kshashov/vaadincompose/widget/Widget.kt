package io.github.kshashov.vaadincompose.widget

abstract class Widget(var key: String? = null) {
    abstract fun createElement(): Element<out Widget>
}
