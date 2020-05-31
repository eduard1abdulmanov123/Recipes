package abdulmanov.eduard.recipes.presentation.ui.recipes.details

import abdulmanov.eduard.recipes.domain.interactors.recipes.GetDetailsRecipeUseCase
import abdulmanov.eduard.recipes.presentation.ui.base.BaseViewModel
import abdulmanov.eduard.recipes.presentation.ui.base.ScreenState
import abdulmanov.eduard.recipes.presentation.ui.recipes.mappers.RecipesPresentationModelMapper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class DetailsRecipeViewModel @Inject constructor(
    private val getDetailsRecipeUseCase: GetDetailsRecipeUseCase,
    private val mapper: RecipesPresentationModelMapper
) : BaseViewModel() {

    var router: Router? = null

    private val _state = MutableLiveData<ScreenState>()
    val state: LiveData<ScreenState>
        get() = _state

    fun loadDetailsRecipe(id: String) {
    }

    fun refresh(id: String) {
    }

    fun onBackPressed() = router?.exit()
}