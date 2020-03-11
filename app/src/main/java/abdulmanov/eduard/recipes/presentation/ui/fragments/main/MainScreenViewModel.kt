package abdulmanov.eduard.recipes.presentation.ui.fragments.main

import abdulmanov.eduard.recipes.domain.interactors.recipes.GetTapeUseCase
import abdulmanov.eduard.recipes.presentation.ui.base.BaseViewModel
import abdulmanov.eduard.recipes.presentation.ui.base.ScreenState
import abdulmanov.eduard.recipes.presentation.ui.mapper.RecipesViewModelMapper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(
    private val getTapeUseCase: GetTapeUseCase,
    private val mapper:RecipesViewModelMapper
):BaseViewModel() {

    private val _state = MutableLiveData<ScreenState>()
    val state:LiveData<ScreenState>
        get() = _state

    init {
        _state.value = ScreenState.StartState
    }

    fun loadTape(){
        _state.postValue(ScreenState.ProgressState)
        getTape()
    }

    fun repeat(){
        _state.postValue(ScreenState.ProgressAfterErrorState)
        getTape()
    }

    private fun getTape(){
        getTapeUseCase.execute()
            .map(mapper::mapTapeToViewModels)
            .safeSubscribe(
                {
                    _state.postValue(ScreenState.DataState(it))
                },
                {
                    _state.postValue(ScreenState.ErrorState(handleError(it)))
                }
            )
    }
}