package abdulmanov.eduard.recipes.presentation.ui.base

import android.util.Log
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable

object Paginator {

    sealed class State{
        object Empty : State()
        object EmptyProgress : State()
        data class EmptyError(val error: Throwable) : State()
        data class EmptyProgressAfterError(val error:Throwable) : State()
        data class Data<T>(val pageCount:Int, val data:List<T>):State()
        object EmptyData : State()
        data class Refresh<T>(val pageCount: Int, val data:List<T>):State()
        data class NewPageProgress<T>(val pageCount: Int, val data:List<T>):State()
        data class NewPageError<T>(val error:Throwable, val pageCount: Int, val data:List<T>):State()
        data class FullData<T>(val pageCount:Int,val data:List<T>):State()

    }

    sealed class Action{
        object Refresh : Action()
        object Restart : Action()
        object LoadMore: Action()

        data class NewPage<T>(val pageNumber:Int, val items:List<T>):Action()
        data class PageError(val error:Throwable):Action()
    }

    sealed class SideEffect{
        data class LoadPage(val currentPage:Int):SideEffect()
        data class ErrorEvent(val error:Throwable):SideEffect()
    }

    private fun <T> reducer(
        action:Action,
        state:State,
        sideEffectListener:(SideEffect) -> Unit
    ):State{
        return when(action){
            is Action.Refresh -> {
                sideEffectListener.invoke(SideEffect.LoadPage(1))
                when(state){
                    is State.Empty -> State.EmptyProgress
                    is State.EmptyError -> State.EmptyProgressAfterError(state.error)
                    is State.Data<*> -> State.Refresh(state.pageCount, state.data)
                    is State.FullData<*> -> State.Refresh(state.pageCount, state.data)
                    is State.NewPageError<*> -> State.NewPageProgress(state.pageCount, state.data)
                    else -> state
                }
            }
            is Action.Restart -> {
                when(state){
                    else -> state
                }
            }
            is Action.LoadMore -> {
                when(state){
                    is State.Data<*> -> {
                        sideEffectListener.invoke(SideEffect.LoadPage(state.pageCount + 1))
                        State.NewPageProgress(state.pageCount, state.data)
                    }
                    else -> state
                }
            }
            is Action.NewPage<*> -> {
                val items = action.items as List<T>
                when(state){
                    is State.EmptyProgress ->{
                        if(items.isEmpty()){
                            State.EmptyData
                        }else{
                            State.Data(1,items)
                        }
                    }
                    is State.EmptyProgressAfterError ->{
                        if(items.isEmpty()){
                            State.EmptyData
                        }else{
                            State.Data(1,items)
                        }
                    }
                    is State.Refresh<*> -> {
                        if(items.isEmpty()){
                            State.EmptyData
                        }else{
                            State.Data(1,items)
                        }
                    }
                    is State.NewPageProgress<*> ->{
                        if(items.isEmpty()){
                            State.FullData(state.pageCount,state.data)
                        }else{
                            State.Data(state.pageCount + 1,state.data as List<T> + items)
                        }
                    }
                    else -> state
                }
            }
            is Action.PageError -> {
                when(state){
                    is State.EmptyProgress -> State.EmptyError(action.error)
                    is State.EmptyProgressAfterError -> State.EmptyError(action.error)
                    is State.Refresh<*> -> {
                        sideEffectListener.invoke(SideEffect.ErrorEvent(action.error))
                        State.Data(state.pageCount,state.data)
                    }
                    is State.NewPageProgress<*> -> {
                        State.NewPageError(action.error,state.pageCount,state.data)
                    }
                    else -> state
                }
            }
        }
    }

    class Store<T>{
        private var state: State = Paginator.State.Empty
        var render: (State) -> Unit = {}
            set(value){
                field = value
                value(state)
            }

        private val sideEffectRelay = PublishRelay.create<SideEffect>()
        val sideEffects: Observable<SideEffect> =
            sideEffectRelay
                .hide()

        fun proceed(action: Action){
            val newState = reducer<T>(action, state) { sideEffect ->
                sideEffectRelay.accept(sideEffect)
            }
            sendMessageLog(state,newState)
            if (newState != state) {
                state = newState
                render(state)
            }
        }

        private fun sendMessageLog(lastState:State,newState:State){
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

            Log.d("Paginator","$lastStateName -> $newStateName")
        }
    }
}