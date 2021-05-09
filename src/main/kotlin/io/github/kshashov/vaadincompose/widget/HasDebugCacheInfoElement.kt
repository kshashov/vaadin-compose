package io.github.kshashov.vaadincompose.widget

interface HasDebugCacheInfoElement {
    fun getDebugCacheInfo(): Map<String, Element<*>>?
}
