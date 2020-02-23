package abdulmanov.eduard.recipes.presentation.ui.model

import com.example.delegateadapter.delegate.diff.IComparableItem


data class LoadingViewModel(
    val id: Int = 0,
    val state: LoadingViewModelState = LoadingViewModelState.Loading
) : IComparableItem {

    override fun id() = id

    override fun content() = this

}

sealed class LoadingViewModelState {
    object Loading : LoadingViewModelState()
    object Refresh : LoadingViewModelState()
}