package io.github.kshashov.vaadincompose.widget

import io.github.kshashov.vaadincompose.BuildContext
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable

class StreamConsumer<T>(val stream: Observable<T>, val builder: (T) -> Widget, key: String? = null) :
    StatefulWidget(key) {
    override fun createState() = StreamConsumerState<T>()

    class StreamConsumerState<T> : WidgetState<StreamConsumer<T>>() {
        private var subscription: Disposable? = null
        private var payload: T? = null

        override fun build(context: BuildContext): Widget {
            return widget.builder.invoke(payload!!)
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
            subscription = widget.stream.subscribe { setState { payload = it } }
        }

        private fun unsubscribe() {
            subscription?.dispose()
        }
    }
}
