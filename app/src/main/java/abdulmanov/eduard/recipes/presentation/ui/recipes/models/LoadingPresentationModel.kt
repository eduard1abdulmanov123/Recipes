package abdulmanov.eduard.recipes.presentation.ui.recipes.models

import com.example.delegateadapter.delegate.diff.IComparableItem

data class LoadingPresentationModel(
    val id: Int = -1,
    var state: LoadingViewModelState = LoadingViewModelState.Loading
) : IComparableItem {

    override fun id() = id

    override fun content() = this

    sealed class LoadingViewModelState {
        object Loading : LoadingViewModelState()
        object Error : LoadingViewModelState()
    }
}