package io.github.kshashov.vaadincompose.debug

import io.github.kshashov.vaadincompose.BuildContext
import io.reactivex.rxjava3.subjects.BehaviorSubject

class DebugToolsBloc {
    private var root: BuildContext? = null
    val debugTree: BehaviorSubject<BuildContext> = BehaviorSubject.create()
    val selected: BehaviorSubject<BuildContext> = BehaviorSubject.create()
    val horizontal: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    fun init(context: BuildContext) {
        if (root == null) {
            root = context
        }

        refresh(context, "init")
    }

    fun refresh(context: BuildContext, code: String) {
        if (!isDebugContext(context)) {
            if (root != null) {
                val buildContext = root!!.findChildElement {
                    it.widget is DebugWindow
                }?.context!!.childs[0].childs[0].childs[0].childs[0].childs[0]
                debugTree.onNext(buildContext)
            }
        }
    }

    fun changeOrientation() {
        horizontal.onNext(!horizontal.value)
    }

    private fun isDebugContext(context: BuildContext): Boolean {
        return context.findParentWidgetByType(DebugPanel::class.java) != null
    }

    fun select(node: BuildContext?) {
        selected.onNext(node)
    }
}
