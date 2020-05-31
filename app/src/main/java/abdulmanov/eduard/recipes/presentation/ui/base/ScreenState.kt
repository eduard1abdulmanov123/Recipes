package abdulmanov.eduard.recipes.presentation.ui.base

sealed class ScreenState {
    object Progress : ScreenState()
    class Data<T>(val data: T) : ScreenState()
    class Error(val message: Int) : ScreenState()
    object ProgressAfterError : ScreenState()
}