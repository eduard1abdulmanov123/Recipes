package abdulmanov.eduard.recipes.presentation.ui.fragments.category

import abdulmanov.eduard.recipes.domain.interactors.recipes.GetRecipesUseCase
import abdulmanov.eduard.recipes.presentation.ui.base.BaseViewModel
import abdulmanov.eduard.recipes.presentation.ui.base.Event
import abdulmanov.eduard.recipes.presentation.ui.base.Paginator
import abdulmanov.eduard.recipes.presentation.ui.mapper.RecipesViewModelMapper
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class CategoryScreenViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val mapper:RecipesViewModelMapper
):BaseViewModel(){

    val paginator = Paginator.Store<RecipeViewModel>()
    var category:String = ""

    private val _state =  MutableLiveData<Paginator.State>()
    val state:LiveData<Paginator.State>
        get() = _state

    private val _message = MutableLiveData<Event<Throwable>>()
    val message:LiveData<Event<Throwable>>
        get() = _message

    private var sideEffectDisposable:Disposable? = null

    init {
        paginator.render = {_state.postValue(it)}
        sideEffectDisposable = paginator.sideEffects.subscribe{
            when(it){
                is Paginator.SideEffect.LoadPage -> loadNewPage(it.currentPage)
                is Paginator.SideEffect.ErrorEvent -> _message.postValue(Event(it.error))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        sideEffectDisposable?.dispose()
    }

    fun refresh() = paginator.proceed(Paginator.Action.Refresh)

    fun loadNextPage()  = paginator.proceed(Paginator.Action.LoadMore)

    private fun loadNewPage(page:Int){
        disposable.clear()
        getRecipesUseCase.execute(category,page)
            .map(mapper::mapRecipesToViewModels)
            .safeSubscribe(
                {
                    paginator.proceed(Paginator.Action.NewPage(page,it))
                },
                {
                    paginator.proceed(Paginator.Action.PageError(it))
                }
            )
    }
}