package io.github.kshashov.vaadincompose.debug

import io.github.kshashov.vaadincompose.BuildContext
import io.reactivex.rxjava3.subjects.PublishSubject

class DebugToolsBloc {
    private var root: BuildContext? = null
    val debugTree: PublishSubject<BuildContext> = PublishSubject.create()

    fun init(context: BuildContext) {
        if (root == null) {
            root = context
        }
    }

    fun refresh(context: BuildContext, code: String) {
        if (!isDebugContext(context)) {
            if (root != null) {
                debugTree.onNext(root)
            }
        }
    }

    private fun isDebugContext(context: BuildContext): Boolean {
        return context.findParentWidgetByType(DebugWidgetsTree::class.java) != null
    }
}
