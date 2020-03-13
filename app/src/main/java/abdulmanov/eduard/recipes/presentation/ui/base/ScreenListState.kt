package abdulmanov.eduard.recipes.presentation.ui.base

sealed class ScreenListState {
    //Состояние, когда начинается начальная загрузка данных
    object StartProgressState : ScreenListState()
    //Состояние, когда при начальной загрузке произошла ошибка
    class StartErrorState(val message: Int) : ScreenListState()
    //Состояние, когда после ошибки при начальной загрузке данных, мы повторно пытаемся получить данные
    object StartProgressAfterErrorState : ScreenListState()
    //Состояние, когда мы при начальной загрузке данных, данные отсутствуют
    object StartEmptyState : ScreenListState()
    //Состояние, когда данные были получены и они не пустые
    class DataState<T>(val data: List<T>) : ScreenListState()
}