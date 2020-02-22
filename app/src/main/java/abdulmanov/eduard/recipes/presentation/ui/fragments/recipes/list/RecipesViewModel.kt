package abdulmanov.eduard.recipes.presentation.ui.fragments.recipes.list

import abdulmanov.eduard.recipes.data.network.DetailsRecipeService
import abdulmanov.eduard.recipes.data.network.RecipesService
import abdulmanov.eduard.recipes.data.repository.RecipesRepositoryImpl
import abdulmanov.eduard.recipes.domain.repositories.RecipesRepository
import abdulmanov.eduard.recipes.presentation.common.ListState
import abdulmanov.eduard.recipes.presentation.ui.base.BaseViewModel
import abdulmanov.eduard.recipes.presentation.ui.mapper.RecipesViewModelMapper
import abdulmanov.eduard.recipes.presentation.ui.mapper.RecipesViewModelMapperImpl
import androidx.lifecycle.MutableLiveData

class RecipesViewModel:BaseViewModel() {

    companion object{
        private const val FIRST_PAGE = 1
    }

    private val repository:RecipesRepository = RecipesRepositoryImpl(RecipesService(), DetailsRecipeService())
    private var mapper:RecipesViewModelMapper = RecipesViewModelMapperImpl()

    val state = MutableLiveData<ListState>().apply { postValue(ListState.EmptyState) }

    private var currentPage :Int = FIRST_PAGE

    fun loadNewPage(){
        if(currentPage == FIRST_PAGE){
            state.postValue(ListState.EmptyProgressState)
            repository.getRecipes("all",currentPage)
                .map(mapper::mapRecipesToViewModels)
                .safeSubscribe(
                    {
                        state.postValue(ListState.DataState(it))
                    },
                    {
                        state.postValue(ListState.EmptyErrorState(handleError(it)))
                    }
                )
        }
    }

    fun refresh(){

    }

    private fun changeState(){

    }

}