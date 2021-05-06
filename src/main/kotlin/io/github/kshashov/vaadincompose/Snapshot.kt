package io.github.kshashov.vaadincompose

class Snapshot<T : Any> private constructor(val data: T?, val error: Throwable?, val state: State = State.NONE) {
    val hasData: Boolean = data != null
    val hasError: Boolean = error != null
    val isDone: Boolean = state == State.DONE

    fun requireData() = data!!
    fun requireError() = error!!

    fun done(): Snapshot<T> = Snapshot(data, error, State.DONE)

    companion object {
        fun <T : Any> withData(data: T?, state: State = State.NONE): Snapshot<T> = Snapshot(data, null, state)
        fun <T : Any> withError(error: Throwable, state: State = State.NONE): Snapshot<T> = Snapshot(null, error, state)
        fun <T : Any> empty(): Snapshot<T> = Snapshot(null, null)
    }

    enum class State {
        NONE,
        ACTIVE,
        DONE
    }
}
