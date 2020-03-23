package abdulmanov.eduard.recipes.presentation.ui.fragments.tape

import abdulmanov.eduard.recipes.domain.interactors.recipes.GetTapeUseCase
import abdulmanov.eduard.recipes.presentation.navigation.Screens
import abdulmanov.eduard.recipes.presentation.ui.base.BaseViewModel
import abdulmanov.eduard.recipes.presentation.ui.base.ScreenState
import abdulmanov.eduard.recipes.presentation.ui.mapper.RecipesViewModelMapper
import abdulmanov.eduard.recipes.presentation.ui.model.CategoryVM
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class TapeViewModel @Inject constructor(
    private val getTapeUseCase: GetTapeUseCase,
    private val mapper:RecipesViewModelMapper
):BaseViewModel() {

    var router:Router? = null

    private val _state = MutableLiveData<ScreenState>()
    val state:LiveData<ScreenState>
        get() = _state

    init {
        _state.value = ScreenState.Start
    }

    fun loadTape(){
        _state.postValue(ScreenState.Progress)
        getTape()
    }

    fun repeat(){
        _state.postValue(ScreenState.ProgressAfterError)
        getTape()
    }

    fun onBackPressed() = router?.exit()

    fun onClickCategoryItem(category:CategoryVM) = router?.navigateTo(Screens.Category(category))

    private fun getTape(){
        getTapeUseCase.execute()
            .map(mapper::mapTapeToViewModels)
            .safeSubscribe(
                {
                    _state.postValue(ScreenState.Data(it))
                },
                {
                    _state.postValue(ScreenState.Data(it))
                }
            )
    }
}