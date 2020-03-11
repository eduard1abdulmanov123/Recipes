package abdulmanov.eduard.recipes.presentation.ui.base

sealed class ScreenState {
    object StartState : ScreenState()
    object ProgressState : ScreenState()
    class DataState<T>(val data: T) : ScreenState()
    class ErrorState(val message: Int) : ScreenState()
    object ProgressAfterErrorState : ScreenState()
}