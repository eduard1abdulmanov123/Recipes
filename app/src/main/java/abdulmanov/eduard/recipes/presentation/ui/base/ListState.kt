package abdulmanov.eduard.recipes.presentation.ui.base

sealed class ListState {
    //Когда человек только зашел на экран, стартовое состояние
    object EmptyState: ListState()
    //Началась загрузка первой страницы
    class EmptyProgressState(val swipeRefresh:Boolean): ListState()
    //Произошла ошибка при загрузки первой страницы, показать layout_error
    class EmptyErrorState(val message:Int): ListState()
    //Начать загрузку первой страницы после ошибки
    object EmptyErrorStateRefresh : ListState()
    //Получили данные
    object DataState : ListState()
    //Пагинация
    object PageProgressState: ListState()
    //Ошибка при пагинации
    object PageErrorState: ListState()
    //Получили все данные
    object AllDataState: ListState()
}