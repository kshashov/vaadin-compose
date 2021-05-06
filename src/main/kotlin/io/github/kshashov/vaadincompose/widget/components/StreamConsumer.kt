package io.github.kshashov.vaadincompose.widget.components

import io.github.kshashov.vaadincompose.BuildContext
import io.github.kshashov.vaadincompose.Snapshot
import io.github.kshashov.vaadincompose.widget.StatefulWidget
import io.github.kshashov.vaadincompose.widget.Widget
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable

class StreamConsumer<T : Any>(
    val stream: Observable<T?>,
    val builder: (Snapshot<T>) -> Widget,
    val initial: Snapshot<T> = Snapshot.empty(),
    key: String? = null
) : StatefulWidget(key) {
    override fun createState() = StreamConsumerState<T>()

    class StreamConsumerState<T : Any> : WidgetState<StreamConsumer<T>>() {
        private var subscription: Disposable? = null
        private var payload: Snapshot<T>? = null
        private var first = true

        override fun build(context: BuildContext): Widget {
            val data = if (first) widget.initial else payload!!
            return widget.builder.invoke(data)
        }

        override fun updateWidget(widget: StatefulWidget) {
            super.updateWidget(widget)
            if (payload == null) {
                payload = this.widget.initial
            }
        }

        override fun firstAttach() {
            super.firstAttach()
            subscribe()
        }

        override fun attach(oldWidget: StatefulWidget) {
            super.attach(oldWidget)
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
            subscription = widget.stream.subscribe({
                first = false
                setState { payload = Snapshot.withData(it, Snapshot.State.ACTIVE) }
            }, {
                first = false
                setState { payload = Snapshot.withError(it, Snapshot.State.ACTIVE) }
            }, {
                setState { payload = payload!!.done() }
            })
        }

        private fun unsubscribe() {
            subscription?.dispose()
        }
    }
}
