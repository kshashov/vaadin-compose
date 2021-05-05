package io.github.kshashov.vaadincompose.widget.components

import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.widget.StatefulWidget
import io.github.kshashov.vaadincompose.widget.Widget
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable

class StreamConsumer<T>(
    val stream: Observable<T>,
    val builder: (T) -> Widget,
    val initial: Widget? = null,
    key: String? = null
) :
    StatefulWidget(key) {
    override fun createState() = StreamConsumerState<T>()

    class StreamConsumerState<T> : WidgetState<StreamConsumer<T>>() {
        private var subscription: Disposable? = null
        private var payload: T? = null
        private var first = true

        override fun build(context: BuildContext): Widget {

            return Container(
                childs = listOf(
                    if (first && (widget.initial != null)) {
                        widget.initial!!
                    } else {
                        widget.builder.invoke(payload!!)
                    }
                )
            )
        }

        override fun initState() {
            super.initState()
            subscribe();
        }

        override fun didUpdateWidget(oldWidget: StatefulWidget) {
            super.didUpdateWidget(oldWidget)
            if ((oldWidget as StreamConsumer<*>).stream != widget.stream) {
                unsubscribe();
                subscribe();
            }
        }

        override fun detach() {
            super.detach()
            unsubscribe()
        }

        private fun subscribe() {
            subscription = widget.stream.subscribe {
                first = false
                setState { payload = it }
            }
        }

        private fun unsubscribe() {
            subscription?.dispose()
        }
    }
}
