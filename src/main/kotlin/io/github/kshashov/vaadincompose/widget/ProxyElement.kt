package io.github.kshashov.vaadincompose.widget

/**
 * Supposed to be used as a parent class for non-render widgets.
 * Propogates render shit from the nested widget.
 */
abstract class ProxyElement<WIDGET : Widget>(widget: WIDGET) : EagerChildsElement<WIDGET>(widget) {

    override fun getChilds(): Collection<Widget> = listOf(getChild())

    abstract fun getChild(): Widget

    override fun proxiedComponentIndex(): Int {
        return 0
    }
}

