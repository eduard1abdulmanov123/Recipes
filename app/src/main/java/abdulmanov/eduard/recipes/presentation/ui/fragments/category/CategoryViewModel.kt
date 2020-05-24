package abdulmanov.eduard.recipes.presentation.ui.fragments.category

import abdulmanov.eduard.recipes.domain.interactors.recipes.GetRecipesUseCase
import abdulmanov.eduard.recipes.presentation.navigation.Screens
import abdulmanov.eduard.recipes.presentation.ui.base.BaseViewModel
import abdulmanov.eduard.recipes.presentation.ui.base.Event
import abdulmanov.eduard.recipes.presentation.ui.base.Paginator
import abdulmanov.eduard.recipes.presentation.ui.mapper.RecipesViewModelMapper
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeVM
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class CategoryViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val mapper:RecipesViewModelMapper
):BaseViewModel() {

    var category: String? = null

    var router: Router? = null

    private val _message = MutableLiveData<Event<Throwable>>()
    val message: LiveData<Event<Throwable>>
        get() = _message

    private val _state = MutableLiveData<Paginator.State>()
    val state: LiveData<Paginator.State>
        get() = _state

    private val paginator = Paginator.Store<RecipeVM>()

    private var pageDisposable: Disposable? = null

    init {
        paginator.render = { _state.postValue(it) }
        paginator.sideEffects.subscribe {
            when (it) {
                is Paginator.SideEffect.LoadPage -> loadNewPage(it.currentPage)
                is Paginator.SideEffect.ErrorEvent -> _message.postValue(Event(it.error))
            }
        }.connect()
    }

    fun refresh() = paginator.proceed(Paginator.Action.Refresh)

    fun repeat() = paginator.proceed(Paginator.Action.Repeat)

    fun loadNextPage() = paginator.proceed(Paginator.Action.LoadMore)

    fun onBackPressed() = router?.exit()

    fun onClickRecipeItem(recipe:RecipeVM) = router?.navigateTo(Screens.DetailsRecipe(recipe))

    private fun loadNewPage(page: Int) {
        if(category != null) {
            pageDisposable?.dispose()
            pageDisposable = getRecipesUseCase.execute(category!!, page)
                .map(mapper::mapRecipesToViewModels)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        paginator.proceed(Paginator.Action.NewPage(page, it))
                    },
                    {
                        paginator.proceed(Paginator.Action.PageError(it))
                    }
                )
            pageDisposable?.connect()
        }else{
            throw NullPointerException("Category should not be empty")
        }
    }
}