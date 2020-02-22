package abdulmanov.eduard.recipes.presentation.common

sealed class ListState {
    object EmptyState:ListState()
    object EmptyProgressState:ListState()
    object EmptyData:ListState()
    class EmptyErrorState(val message:Int):ListState()
    class DataState<T>(val data:List<T>):ListState()
    object PageProgressState:ListState()
    class PageErrorState(val message:Int):ListState()
    object AllDataState:ListState()
}