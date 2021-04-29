package io.github.kshashov.vaadincompose.utils

class BooleanPairHolder<T> {
    private var trueElement: T? = null
    private var falseElement: T? = null

    fun getElement(condition: Boolean) = if (condition) trueElement else falseElement
    fun containsElement(condition: Boolean) = if (condition) trueElement != null else falseElement != null
    fun putElement(condition: Boolean, element: T) {
        if (condition) {
            trueElement = element
        } else {
            falseElement = element
        }
    }
}
