package abdulmanov.eduard.recipes.presentation.ui.base.paginator

interface State<T>{
    fun refresh() {}
    fun loadNewPage() {}
    fun newData(data: List<T>) {}
    fun fail(error: Throwable) {}
}