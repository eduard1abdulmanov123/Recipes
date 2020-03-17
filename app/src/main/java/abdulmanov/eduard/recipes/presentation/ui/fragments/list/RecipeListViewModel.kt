package abdulmanov.eduard.recipes.presentation.ui.fragments.list

import abdulmanov.eduard.recipes.domain.interactors.recipes.GetRecipesUseCase
import abdulmanov.eduard.recipes.presentation.ui.base.ListState
import abdulmanov.eduard.recipes.presentation.ui.base.BaseViewModel
import abdulmanov.eduard.recipes.presentation.ui.mapper.RecipesViewModelMapper
import abdulmanov.eduard.recipes.presentation.ui.model.LoadingViewModel
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeViewModel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.delegateadapter.delegate.diff.IComparableItem
import javax.inject.Inject

class RecipeListViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val mapper:RecipesViewModelMapper
):BaseViewModel() {

    companion object{
        private const val FIRST_PAGE = 1
    }

    val data = MutableLiveData<List<IComparableItem>>()
    val state = MutableLiveData<ListState>()
    var category:String = ""

    private var currentPage :Int = FIRST_PAGE

    init {
        Log.d("RecipeListViewModel","init")
        state.value = ListState.EmptyState
    }

    fun loadNewPage(){
        state.postValue(ListState.PageProgressState)
        data.postValue(addLoadingViewModel())
        /*getRecipes(
            {
                if(it.isNotEmpty()) {
                    currentPage++
                    data.postValue(removeLoadingViewModel().plus(it).distinctBy { it.id() })
                    state.postValue(ListState.DataState)
                }else{
                    data.postValue(removeLoadingViewModel())
                    state.postValue(ListState.AllDataState)
                }
            },
            {
                data.postValue(changeStateLoadingViewModel(LoadingViewModel.Refresh))
                state.postValue(ListState.PageErrorState)
            }
        )*/
    }

    fun refresh(){
       /* when(state.value){
            is ListState.EmptyErrorState->{
                state.postValue(ListState.EmptyErrorStateRefresh)
                getRecipes(
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
            is ListState.PageErrorState->{
                data.postValue(changeStateLoadingViewModel(LoadingViewModel.Loading))
                state.postValue(ListState.PageProgressState)
                getRecipes(
                    {
                        currentPage++
                        data.postValue(removeLoadingViewModel().plus(it).distinctBy { it.id() })
                        state.postValue(ListState.DataState)
                    },
                    {
                        data.postValue(changeStateLoadingViewModel(LoadingViewModel.Refresh))
                        state.postValue(ListState.PageErrorState)
                    }
                )
            }
        }*/
    }

    fun restart(){
        when(state.value){
            is ListState.EmptyState ->{
                state.postValue(ListState.EmptyProgressState(false))
                getRecipes(
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
                getRecipes(
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
            is ListState.AllDataState-> {
                currentPage = FIRST_PAGE
                state.postValue(ListState.EmptyProgressState(true))
                getRecipes(
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

    private fun getRecipes(onSuccess:(List<RecipeViewModel>)->Unit,onError:(Throwable)->Unit){
        getRecipesUseCase.execute(category,currentPage)
            .map(mapper::mapRecipesToViewModels)
            .safeSubscribe(onSuccess,onError)
    }

    private fun addLoadingViewModel():List<IComparableItem>{
        return data.value!!.plus(LoadingViewModel())
    }

    private fun removeLoadingViewModel():List<IComparableItem>{
        return data.value!!.minus(data.value!!.last())
    }

    /*private fun changeStateLoadingViewModel(state:LoadingViewModel):List<IComparableItem>{
        val newLoadingViewModel = (data.value!!.last() as LoadingViewModel).copy(state = state)
        return data.value!!.minus(data.value!!.last()).plus(newLoadingViewModel)
    }*/
}
