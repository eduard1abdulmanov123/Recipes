package abdulmanov.eduard.recipes.presentation.ui.base

import android.util.AndroidException
import android.util.Log
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

object Paginator {

    sealed class State {
        object Empty : State()
        object EmptyProgress : State()
        data class EmptyError(val error: Throwable) : State()
        data class EmptyProgressAfterError(val error: Throwable) : State()
        data class Data<T>(val pageCount: Int, val data: List<T>) : State()
        data class Refresh<T>(val pageCount: Int, val data: List<T>) : State()
        data class NewPageProgress<T>(val pageCount: Int, val data: List<T>) : State()
        data class NewPageError<T>(val pageCount: Int, val data: List<T>) : State()
        data class FullData<T>(val pageCount: Int, val data: List<T>) : State()
        data class RefreshAfterFullData<T>(val pageCount: Int, val data: List<T>) : State()
    }

    sealed class Action {
        object Refresh : Action()
        object Repeat : Action()
        object LoadMore : Action()
        data class NewPage<T>(val pageNumber: Int, val items: List<T>) : Action()
        data class PageError(val error: Throwable) : Action()
    }

    sealed class SideEffect {
        data class LoadPage(val currentPage: Int) : SideEffect()
        data class ErrorEvent(val error: Throwable) : SideEffect()
    }

    private fun <T> reducer(
        action: Action,
        state: State,
        sideEffectListener: (SideEffect) -> Unit
    ): State {
        return when (action) {
            is Action.Refresh -> {
                when (state) {
                    is State.Empty -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(1))
                        State.EmptyProgress
                    }
                    is State.Data<*> -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(1))
                        State.Refresh(state.pageCount, state.data)
                    }
                    is State.NewPageProgress<*> -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(1))
                        State.Refresh(state.pageCount, state.data)
                    }
                    is State.NewPageError<*> -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(1))
                        State.Refresh(state.pageCount, state.data)
                    }
                    is State.FullData<*> -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(1))
                        State.RefreshAfterFullData(state.pageCount, state.data)
                    }
                    else -> state
                }
            }
            is Action.Repeat -> {
                when (state) {
                    is State.EmptyError -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(1))
                        State.EmptyProgressAfterError(state.error)
                    }
                    is State.NewPageError<*> -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(state.pageCount + 1))
                        State.NewPageProgress(state.pageCount, state.data)
                    }
                    else -> state
                }
            }
            is Action.LoadMore -> {
                when (state) {
                    is State.Data<*> -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(state.pageCount + 1))
                        State.NewPageProgress(state.pageCount, state.data)
                    }
                    is State.Refresh<*> -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(state.pageCount + 1))
                        State.NewPageProgress(state.pageCount, state.data)
                    }
                    else -> state
                }
            }
            is Action.NewPage<*> -> {
                val items = action.items
                when (state) {
                    is State.EmptyProgress -> State.Data(1, items)
                    is State.EmptyProgressAfterError -> State.Data(1, items)
                    is State.Refresh<*> -> State.Data(1, items)
                    is State.NewPageProgress<*> -> {
                        if (items.isEmpty()) {
                            State.FullData(state.pageCount, state.data)
                        } else {
                            State.Data(state.pageCount + 1, (state.data + items).distinct())
                        }
                    }
                    is State.RefreshAfterFullData<*> -> State.Data(1, items)
                    else -> state
                }
            }
            is Action.PageError -> {
                when (state) {
                    is State.EmptyProgress -> State.EmptyError(action.error)
                    is State.EmptyProgressAfterError -> State.EmptyError(action.error)
                    is State.Refresh<*> -> {
                        sideEffectListener.invoke(SideEffect.ErrorEvent(action.error))
                        State.Data(state.pageCount, state.data)
                    }
                    is State.NewPageProgress<*> -> State.NewPageError(state.pageCount, state.data)
                    is State.RefreshAfterFullData<*> -> {
                        sideEffectListener.invoke(SideEffect.ErrorEvent(action.error))
                        State.FullData(state.pageCount, state.data)
                    }
                    else -> state
                }
            }
        }
    }

    class Store<T> {
        private var state: State = State.Empty
        var render: (State) -> Unit = {}
            set(value) {
                field = value
                value(state)
            }

        private val sideEffectRelay = PublishRelay.create<SideEffect>()
        val sideEffects: Observable<SideEffect> =
            sideEffectRelay
                .hide()
                .observeOn(AndroidSchedulers.mainThread())

        fun proceed(action: Action) {
            val newState = reducer<T>(action, state) { sideEffect ->
                sideEffectRelay.accept(sideEffect)
            }
            sendMessageLog(state, newState)
            if (newState != state) {
                state = newState
                render(state)
            }
        }

        private fun sendMessageLog(lastState: State, newState: State) {
            val lastStateName = lastState::class.toString()
                .split("$")
                .last()
                .split(" ")
                .first()

            val newStateName = newState::class.toString()
                .split("$")
                .last()
                .split(" ")
                .first()

            Log.d("Paginator", "$lastStateName -> $newStateName")
        }
    }
}