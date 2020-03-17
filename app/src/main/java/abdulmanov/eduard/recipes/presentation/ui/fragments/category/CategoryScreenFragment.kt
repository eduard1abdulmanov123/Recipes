package abdulmanov.eduard.recipes.presentation.ui.fragments.category

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.app.BaseApp
import abdulmanov.eduard.recipes.presentation.common.handleError
import abdulmanov.eduard.recipes.presentation.common.visibilityGone
import abdulmanov.eduard.recipes.presentation.ui.adapters.LoadingDelegateAdapter
import abdulmanov.eduard.recipes.presentation.ui.adapters.RecipesDelegateAdapter
import abdulmanov.eduard.recipes.presentation.ui.base.*
import abdulmanov.eduard.recipes.presentation.ui.model.LoadingViewModel
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import com.example.delegateadapter.delegate.diff.IComparableItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_category_screen.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_progress_bar.*
import javax.inject.Inject


class CategoryScreenFragment : Fragment(R.layout.fragment_category_screen) {

    companion object{
        private const val CATEGORY_NAME = "CATEGORY"

        fun newInstance(categoryName:String):CategoryScreenFragment{
            return CategoryScreenFragment().apply {
                arguments = Bundle().apply {
                    putString(CATEGORY_NAME,categoryName)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: CategoryScreenViewModel

    private var loadingItem:LoadingViewModel = LoadingViewModel()
    private lateinit var scrollListener:LinearInfiniteScrollListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BaseApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

        viewModel = ViewModelProvider(this,viewModelFactory).get(CategoryScreenViewModel::class.java)
        viewModel.state.observe(viewLifecycleOwner, Observer(this::updateState))
        viewModel.message.observe(viewLifecycleOwner, Observer{
            it.getContentIfNotHandled()?.let { error ->
                showMessage(error)
            }
        })
        viewModel.category = requireArguments().getString(CATEGORY_NAME)!!

        if(savedInstanceState == null)
            viewModel.refresh()
    }

    private fun initUI(){
        initToolbar()
        initSwipeRefresh()
        initCategoryRecyclerView()
        initErrorRepeatButton()
    }

    private fun initToolbar(){
        category_toolbar.run {
            setTitle(R.string.tape_title)
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                //TODO
            }
        }
    }

    private fun initSwipeRefresh(){
        category_content_swipe_refresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun initCategoryRecyclerView(){
        category_recycler_view.run {
            val linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            scrollListener = LinearInfiniteScrollListener(linearLayoutManager,0){
                viewModel.loadNextPage()
            }
            setHasFixedSize(true)
            addItemDecoration(VerticalItemDecoration(16,8,requireContext()))
            addOnScrollListener(scrollListener)
            layoutManager = linearLayoutManager
            adapter = DiffUtilCompositeAdapter.Builder()
                .add(RecipesDelegateAdapter{})
                .add(LoadingDelegateAdapter{
                    viewModel.refresh()
                })
                .build()
        }
    }

    private fun initErrorRepeatButton(){
        error_repeat_button.setOnClickListener {
            viewModel.refresh()
        }
    }

    private fun updateState(state: Paginator.State) {
        when(state){
            is Paginator.State.Empty -> {
                category_content_swipe_refresh.isRefreshing = false
                category_content_swipe_refresh.visibilityGone(false)
                layout_error.visibilityGone(false)
                layout_progress_bar.visibilityGone(false)
            }
            is Paginator.State.EmptyProgress -> {
                category_content_swipe_refresh.isRefreshing = false
                category_content_swipe_refresh.visibilityGone(false)
                layout_error.visibilityGone(false)
                layout_progress_bar.visibilityGone(true)
            }
            is Paginator.State.EmptyError -> {
                category_content_swipe_refresh.isRefreshing = false
                category_content_swipe_refresh.visibilityGone(false)
                layout_progress_bar.visibilityGone(false)
                layout_error.visibilityGone(true)
                error_repeat_button.visibilityGone(true)
                error_repeat_progress_bar.visibilityGone(false)
                error_secondary_message.setText(state.error.handleError())
            }
            is Paginator.State.EmptyProgressAfterError -> {
                category_content_swipe_refresh.isRefreshing = false
                category_content_swipe_refresh.visibilityGone(false)
                layout_progress_bar.visibilityGone(false)
                layout_error.visibilityGone(true)
                error_repeat_progress_bar.visibilityGone(true)
                error_repeat_button.visibilityGone(false)
                error_secondary_message.setText(state.error.handleError())
            }
            is Paginator.State.Data<*> -> {
                scrollListener.state = LinearInfiniteScrollListener.ScrollState.Normal
                category_content_swipe_refresh.isRefreshing = false
                layout_error.visibilityGone(false)
                layout_progress_bar.visibilityGone(false)
                category_content_swipe_refresh.visibilityGone(true)
                (category_recycler_view.adapter as DiffUtilCompositeAdapter).swapData(state.data as List<IComparableItem>)
            }
            is Paginator.State.NewPageProgress<*> -> {
                scrollListener.state = LinearInfiniteScrollListener.ScrollState.Loading
                category_content_swipe_refresh.isRefreshing = false
                layout_error.visibilityGone(false)
                layout_progress_bar.visibilityGone(false)
                category_content_swipe_refresh.visibilityGone(true)
                loadingItem = loadingItem.copy(state = LoadingViewModel.LoadingViewModelState.Loading)
                (category_recycler_view.adapter as DiffUtilCompositeAdapter).swapData((state.data as List<IComparableItem> + loadingItem as IComparableItem))
            }
            is Paginator.State.NewPageError<*> -> {
                scrollListener.state = LinearInfiniteScrollListener.ScrollState.Loading
                category_content_swipe_refresh.isRefreshing = false
                layout_error.visibilityGone(false)
                layout_progress_bar.visibilityGone(false)
                category_content_swipe_refresh.visibilityGone(true)
                loadingItem = loadingItem.copy(state = LoadingViewModel.LoadingViewModelState.Error)
                (category_recycler_view.adapter as DiffUtilCompositeAdapter).swapData((state.data as List<IComparableItem> + loadingItem as IComparableItem))
            }
            is Paginator.State.FullData<*> -> {
                scrollListener.state = LinearInfiniteScrollListener.ScrollState.Full
                category_content_swipe_refresh.isRefreshing = false
                layout_error.visibilityGone(false)
                layout_progress_bar.visibilityGone(false)
                category_content_swipe_refresh.visibilityGone(true)
                (category_recycler_view.adapter as DiffUtilCompositeAdapter).swapData((state.data as List<IComparableItem>))
            }
        }
    }

    private fun showMessage(error: Throwable){
        Snackbar.make(category_content_swipe_refresh,error.handleError(),Snackbar.LENGTH_SHORT).show()
    }
}
