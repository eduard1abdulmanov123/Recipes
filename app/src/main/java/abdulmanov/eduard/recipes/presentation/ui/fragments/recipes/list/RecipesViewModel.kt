package abdulmanov.eduard.recipes.presentation.ui.fragments.recipes.list

import abdulmanov.eduard.recipes.presentation.common.ListState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecipesViewModel:ViewModel() {

    companion object{
        private const val FIRST_PAGE = 1
    }

    val state = MutableLiveData<ListState>().apply { postValue(ListState.EmptyState) }

    private var currentPage :Int = FIRST_PAGE

    fun loadNewPage(){
        if(currentPage == FIRST_PAGE){
            state.postValue(ListState.EmptyProgressState)
        }
    }

    fun refresh(){

    }

    private fun changeState(){

    }

}