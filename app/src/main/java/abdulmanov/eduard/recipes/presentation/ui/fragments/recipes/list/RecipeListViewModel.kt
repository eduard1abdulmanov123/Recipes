package abdulmanov.eduard.recipes.presentation.ui.fragments.recipes.list

import abdulmanov.eduard.recipes.data.network.DetailsRecipeService
import abdulmanov.eduard.recipes.data.network.RecipesService
import abdulmanov.eduard.recipes.data.repository.RecipesRepositoryImpl
import abdulmanov.eduard.recipes.domain.repositories.RecipesRepository
import abdulmanov.eduard.recipes.presentation.ui.base.ListState
import abdulmanov.eduard.recipes.presentation.ui.base.BaseViewModel
import abdulmanov.eduard.recipes.presentation.ui.mapper.RecipesViewModelMapper
import abdulmanov.eduard.recipes.presentation.ui.mapper.RecipesViewModelMapperImpl
import abdulmanov.eduard.recipes.presentation.ui.model.LoadingViewModel
import abdulmanov.eduard.recipes.presentation.ui.model.LoadingViewModelState
import android.graphics.Insets.add
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.delegateadapter.delegate.diff.IComparableItem

class RecipeListViewModel:BaseViewModel() {

    companion object{
        private const val FIRST_PAGE = 1
    }

    private val repository:RecipesRepository = RecipesRepositoryImpl(RecipesService(), DetailsRecipeService())
    private var mapper:RecipesViewModelMapper = RecipesViewModelMapperImpl()

    val data = MutableLiveData<List<IComparableItem>>()
    val state = MutableLiveData<ListState>()

    private var currentPage :Int = FIRST_PAGE

    init {
        state.value = ListState.EmptyState
        restart()
    }

    fun loadNewPage(){
        state.postValue(ListState.PageProgressState)
        data.postValue(addLoadingViewModel())
        repository.getRecipes("zagotovki",currentPage)
            .map(mapper::mapRecipesToViewModels)
            .safeSubscribe(
                {
                    Log.d("RecipeListViewModel","${it.size}")
                    if(it.isNotEmpty()) {
                        currentPage++
                        data.postValue(removeLoadingViewModel().plus(it).distinctBy { it.id() })
                        state.postValue(ListState.DataState)
                    }else{
                        state.postValue(ListState.AllDataState)
                    }
                },
                {
                    data.postValue(changeStateLoadingViewModel(LoadingViewModelState.Refresh))
                    state.postValue(ListState.PageErrorState)
                }
            )
    }

    fun refresh(){
        when(state.value){
            is ListState.EmptyErrorState->{
                state.postValue(ListState.EmptyErrorStateRefresh)
                repository.getRecipes("zagotovki",currentPage)
                    .map(mapper::mapRecipesToViewModels)
                    .safeSubscribe(
                        {
                            state.postValue(ListState.DataState)
                        },
                        {
                            state.postValue(ListState.EmptyErrorState(handleError(it)))
                        }
                    )
            }
            is ListState.PageErrorState->{
                data.postValue(changeStateLoadingViewModel(LoadingViewModelState.Loading))
                state.postValue(ListState.PageProgressState)
                repository.getRecipes("zagotovki",currentPage)
                    .map(mapper::mapRecipesToViewModels)
                    .safeSubscribe(
                        {
                            currentPage++
                            data.postValue(removeLoadingViewModel().plus(it).distinctBy { it.id() })
                            state.postValue(ListState.DataState)
                        },
                        {
                            /*val newList = data.value!!.minus(loadingViewModel).apply {
                                loadingViewModel.state = "Refresh"
                            }.plus(loadingViewModel)*/
                            data.postValue(changeStateLoadingViewModel(LoadingViewModelState.Refresh))
                            state.postValue(ListState.PageErrorState)
                        }
                    )
            }
        }
    }

    fun restart(){
        when(state.value){
            is ListState.EmptyState ->{
                state.postValue(ListState.EmptyProgressState(false))
                repository.getRecipes("zagotovki",currentPage)
                    .map(mapper::mapRecipesToViewModels)
                    .safeSubscribe(
                        {
                            currentPage++
                            data.postValue(it)
                            state.postValue(ListState.DataState)
                        },
                        {
                            state.postValue(ListState.EmptyErrorState(handleError(it)))
                        }
                    )
            }
            is ListState.DataState-> {
                currentPage = FIRST_PAGE
                state.postValue(ListState.EmptyProgressState(true))
                repository.getRecipes("vypechka-deserty",currentPage)
                    .map(mapper::mapRecipesToViewModels)
                    .safeSubscribe(
                        {
                            currentPage++
                            data.postValue(it)
                            state.postValue(ListState.DataState)
                        },
                        {
                            state.postValue(ListState.EmptyErrorState(handleError(it)))
                        }
                    )
            }
        }
    }

    private fun addLoadingViewModel():List<IComparableItem>{
        return data.value!!.plus(LoadingViewModel())
    }

    private fun removeLoadingViewModel():List<IComparableItem>{
        return data.value!!.minus(data.value!!.last())
    }

    private fun changeStateLoadingViewModel(state:LoadingViewModelState):List<IComparableItem>{
        val newLoadingViewModel = (data.value!!.last() as LoadingViewModel).copy(state = state)
        return data.value!!.minus(data.value!!.last()).plus(newLoadingViewModel)
    }
}