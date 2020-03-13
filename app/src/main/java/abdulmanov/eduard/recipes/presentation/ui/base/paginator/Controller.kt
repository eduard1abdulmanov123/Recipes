package abdulmanov.eduard.recipes.presentation.ui.base.paginator

interface Controller<VM> {

    fun changeState(newState: State<VM>)

    fun showMessage(message:Int)

}