package abdulmanov.eduard.recipes.presentation.ui.fragments.category

import abdulmanov.eduard.recipes.domain.interactors.recipes.GetRecipesUseCase
import abdulmanov.eduard.recipes.domain.models.Recipe
import abdulmanov.eduard.recipes.presentation.common.handleError
import abdulmanov.eduard.recipes.presentation.ui.base.ScreenListState
import abdulmanov.eduard.recipes.presentation.ui.base.paginator.Controller
import abdulmanov.eduard.recipes.presentation.ui.base.paginator.Paginator
import abdulmanov.eduard.recipes.presentation.ui.base.paginator.State
import abdulmanov.eduard.recipes.presentation.ui.mapper.RecipesViewModelMapper
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import javax.inject.Inject

class CategoryScreenViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val mapper:RecipesViewModelMapper
):ViewModel(), Controller<RecipeViewModel>{

    companion object{
        private const val FIRST_PAGE = 1
    }

    var category:String = ""

    private val paginator =
        Paginator(
            this::getRecipes,
            mapper::mapRecipesToViewModels,
            this
        )

    private val _state =  MutableLiveData<ScreenListState>()
    val state:LiveData<ScreenListState>
        get() = _state


    override fun onCleared() {
        paginator.release()
    }

    override fun changeState(newState: State<RecipeViewModel>) {
        Log.d("CategoryScreen",newState.toString())
        _state.postValue(
            when(newState){
                is Paginator<*,*>.StartProgressState -> ScreenListState.StartProgressState
                is Paginator<*,*>.StartErrorState -> ScreenListState.StartErrorState(newState.error.handleError())
                is Paginator<*,*>.StartProgressAfterErrorState -> ScreenListState.StartProgressAfterErrorState
                is Paginator<*,*>.StartEmptyState -> ScreenListState.StartEmptyState
                is Paginator<*,*>.DataState -> ScreenListState.DataState(newState.data)
                else -> null
            }
        )
    }

    override fun showMessage(message: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun refresh(){
        paginator.refresh()
    }

    fun loadNewPage(){
        paginator.loadNewPage()
    }

    private fun getRecipes(page:Int): Single<List<Recipe>>{
        return getRecipesUseCase.execute(category, page)
    }

}