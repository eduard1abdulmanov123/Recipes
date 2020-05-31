package abdulmanov.eduard.recipes.presentation.ui.fragments.tape

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.domain.interactors.recipes.GetTapeUseCase
import abdulmanov.eduard.recipes.presentation.navigation.Screens
import abdulmanov.eduard.recipes.presentation.ui.base.BaseViewModel
import abdulmanov.eduard.recipes.presentation.ui.base.ScreenState
import abdulmanov.eduard.recipes.presentation.ui.mappers.RecipesPresentationModelMapper
import abdulmanov.eduard.recipes.presentation.ui.models.CategoryPresentationModel
import abdulmanov.eduard.recipes.presentation.ui.models.RecipePresentationModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class TapeViewModel @Inject constructor(
    private val useCase: GetTapeUseCase,
    private val mapper:RecipesPresentationModelMapper
):BaseViewModel() {

    var router: Router? = null

    private val _state = MutableLiveData<ScreenState>()
    val state: LiveData<ScreenState>
        get() = _state

    fun loadTape(){
        _state.value = ScreenState.Progress
        getTape()
    }

    fun repeat(){
        _state.value = ScreenState.ProgressAfterError
        getTape()
    }

    private fun getTape(){
        useCase.execute()
            .map(mapper::mapTapeToPresentationModel)
            .safeSubscribe(
                {
                    _state.value = ScreenState.Data(it)
                },
                {
                    _state.value = ScreenState.Error(R.string.error_network)
                }
            )
    }

    fun onBackPressed() = router?.exit()

    fun onClickCategoryItem(category:CategoryPresentationModel) = router?.navigateTo(Screens.Category(category))

    fun onClickBestRecipeItem(recipe:RecipePresentationModel) = router?.navigateTo(Screens.DetailsRecipe(recipe))

}