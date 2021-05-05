package io.github.kshashov.vaadincompose.debug

import io.github.kshashov.vaadincompose.BuildContext
import io.reactivex.rxjava3.subjects.BehaviorSubject

class DebugToolsBloc {
    private var root: BuildContext? = null
    val debugTree: BehaviorSubject<BuildContext> = BehaviorSubject.create()

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
        if (context.element.widget is DebugWindow) {
            return true
        }

        if (context.parent == null) {
            return false
        }

        return isDebugContext(context.parent)
    }
}
