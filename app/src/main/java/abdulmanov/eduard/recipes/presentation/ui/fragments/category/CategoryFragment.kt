package abdulmanov.eduard.recipes.presentation.ui.fragments.category

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.app.BaseApp
import abdulmanov.eduard.recipes.presentation.common.handleError
import abdulmanov.eduard.recipes.presentation.common.visibilityGone
import abdulmanov.eduard.recipes.presentation.navigation.BackButtonListener
import abdulmanov.eduard.recipes.presentation.navigation.RouterProvide
import abdulmanov.eduard.recipes.presentation.ui.adapters.LoadingDelegateAdapter
import abdulmanov.eduard.recipes.presentation.ui.adapters.RecipesDelegateAdapter
import abdulmanov.eduard.recipes.presentation.ui.base.*
import abdulmanov.eduard.recipes.presentation.ui.model.CategoryVM
import abdulmanov.eduard.recipes.presentation.ui.model.LoadingVM
import abdulmanov.eduard.recipes.presentation.ui.model.LoadingVM.LoadingViewModelState
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
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_progress_bar.*
import javax.inject.Inject

class CategoryFragment : Fragment(R.layout.fragment_category), BackButtonListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: CategoryViewModel

    private lateinit var category: CategoryVM

    private lateinit var adapter: DiffUtilCompositeAdapter
    private lateinit var scrollListener: LinearInfiniteScrollListener
    private var loadingItem = LoadingVM()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BaseApp).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = requireArguments().getParcelable(CATEGORY)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CategoryViewModel::class.java)
            .apply {
                category = this@CategoryFragment.category.value
                router = (parentFragment as RouterProvide).getRouter()

                message.observe(
                    viewLifecycleOwner,
                    Observer(this@CategoryFragment::showMessage)
                )
                state.observe(
                    viewLifecycleOwner,
                    Observer(this@CategoryFragment::updateState)
                )
            }

        initUI()

        if (savedInstanceState == null) {
            viewModel.refresh()
        }
    }

    override fun onBackPressed(): Boolean {
        viewModel.onBackPressed()
        return true
    }

    private fun initUI() {
        initToolbar()
        initSwipeRefresh()
        initCategoryRecyclerView()
        initErrorRepeatButton()
    }

    private fun initToolbar() {
        category_toolbar.run {
            title = category.name
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                viewModel.onBackPressed()
            }
        }
    }

    private fun initSwipeRefresh() {
        category_content_swipe_refresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun initCategoryRecyclerView() {
        category_recycler_view.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            initScrollListener(layoutManager as LinearLayoutManager)
            initAdapter()
            setHasFixedSize(true)
            addItemDecoration(VerticalItemDecoration(16, 8, requireContext()))
            addOnScrollListener(scrollListener)
            adapter = this@CategoryFragment.adapter
        }
    }

    private fun initScrollListener(linearLayoutManager: LinearLayoutManager) {
        scrollListener = LinearInfiniteScrollListener(linearLayoutManager, 0) {
            viewModel.loadNextPage()
        }
    }

    private fun initAdapter() {
        adapter = DiffUtilCompositeAdapter.Builder()
            .add(RecipesDelegateAdapter(viewModel::onClickRecipeItem))
            .add(LoadingDelegateAdapter(viewModel::repeat))
            .build()
    }

    private fun initErrorRepeatButton() {
        error_repeat_button.setOnClickListener {
            viewModel.repeat()
        }
    }

    private fun showMessage(error: Event<Throwable>) {
        error.getContentIfNotHandled()?.let {
            Snackbar.make(category_root, it.handleError(), Snackbar.LENGTH_SHORT).show()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun updateState(state: Paginator.State) {
        when (state) {
            is Paginator.State.Empty -> establishEmptyState()
            is Paginator.State.EmptyProgress -> establishEmptyProgressState()
            is Paginator.State.EmptyError -> establishEmptyErrorState(state.error)
            is Paginator.State.EmptyProgressAfterError -> establishEmptyProgressAfterErrorState(
                state.error
            )
            is Paginator.State.Data<*> -> establishDataState(state.data as List<IComparableItem>)
            is Paginator.State.NewPageProgress<*> -> establishNewPageProgressState(state.data as List<IComparableItem>)
            is Paginator.State.NewPageError<*> -> establishNewPageErrorState(state.data as List<IComparableItem>)
            is Paginator.State.FullData<*> -> establishFullDataState(state.data as List<IComparableItem>)
            is Paginator.State.Refresh<*> -> establishRefreshState(state.data as List<IComparableItem>)
            is Paginator.State.RefreshAfterFullData<*> -> establishRefreshAfterFullDataState(state.data as List<IComparableItem>)
        }
    }

    private fun establishEmptyProgressState() {
        scrollListener.state = LinearInfiniteScrollListener.PaginationState.Ban
        category_content_swipe_refresh.isRefreshing = false
        category_content_swipe_refresh.visibilityGone(false)
        layout_progress_bar.visibilityGone(true)
        layout_error.visibilityGone(false)
        error_repeat_progress_bar.visibilityGone(false)
        error_repeat_button.visibilityGone(false)
    }

    private fun establishEmptyState() {
        scrollListener.state = LinearInfiniteScrollListener.PaginationState.Ban
        category_content_swipe_refresh.isRefreshing = false
        category_content_swipe_refresh.visibilityGone(false)
        layout_progress_bar.visibilityGone(false)
        layout_error.visibilityGone(false)
        error_repeat_progress_bar.visibilityGone(false)
        error_repeat_button.visibilityGone(false)
    }

    private fun establishEmptyErrorState(error: Throwable) {
        scrollListener.state = LinearInfiniteScrollListener.PaginationState.Ban
        category_content_swipe_refresh.isRefreshing = false
        category_content_swipe_refresh.visibilityGone(false)
        layout_progress_bar.visibilityGone(false)
        layout_error.visibilityGone(true)
        error_repeat_progress_bar.visibilityGone(false)
        error_repeat_button.visibilityGone(true)
        error_secondary_message.setText(error.handleError())
    }

    private fun establishEmptyProgressAfterErrorState(error: Throwable) {
        scrollListener.state = LinearInfiniteScrollListener.PaginationState.Ban
        category_content_swipe_refresh.isRefreshing = false
        category_content_swipe_refresh.visibilityGone(false)
        layout_progress_bar.visibilityGone(false)
        layout_error.visibilityGone(true)
        error_repeat_progress_bar.visibilityGone(true)
        error_repeat_button.visibilityGone(false)
        error_secondary_message.setText(error.handleError())
    }

    private fun establishDataState(data: List<IComparableItem>) {
        scrollListener.state = LinearInfiniteScrollListener.PaginationState.Allow
        category_content_swipe_refresh.isRefreshing = false
        category_content_swipe_refresh.visibilityGone(true)
        layout_progress_bar.visibilityGone(false)
        layout_error.visibilityGone(false)
        error_repeat_progress_bar.visibilityGone(false)
        error_repeat_button.visibilityGone(false)
        adapter.swapData(data)
    }

    private fun establishNewPageProgressState(data: List<IComparableItem>) {
        scrollListener.state = LinearInfiniteScrollListener.PaginationState.Ban
        category_content_swipe_refresh.isRefreshing = false
        category_content_swipe_refresh.visibilityGone(true)
        layout_progress_bar.visibilityGone(false)
        layout_error.visibilityGone(false)
        error_repeat_progress_bar.visibilityGone(false)
        error_repeat_button.visibilityGone(false)
        loadingItem = loadingItem.copy(state = LoadingViewModelState.Loading)
        adapter.swapData(data + loadingItem)
    }

    private fun establishNewPageErrorState(data: List<IComparableItem>) {
        scrollListener.state = LinearInfiniteScrollListener.PaginationState.Ban
        category_content_swipe_refresh.isRefreshing = false
        category_content_swipe_refresh.visibilityGone(true)
        layout_progress_bar.visibilityGone(false)
        layout_error.visibilityGone(false)
        error_repeat_progress_bar.visibilityGone(false)
        error_repeat_button.visibilityGone(false)
        loadingItem = loadingItem.copy(state = LoadingViewModelState.Error)
        adapter.swapData(data + loadingItem)
    }

    private fun establishFullDataState(data: List<IComparableItem>) {
        scrollListener.state = LinearInfiniteScrollListener.PaginationState.Ban
        category_content_swipe_refresh.isRefreshing = false
        category_content_swipe_refresh.visibilityGone(true)
        layout_progress_bar.visibilityGone(false)
        layout_error.visibilityGone(false)
        error_repeat_progress_bar.visibilityGone(false)
        error_repeat_button.visibilityGone(false)
        adapter.swapData(data)
    }

    private fun establishRefreshState(data: List<IComparableItem>) {
        scrollListener.state = LinearInfiniteScrollListener.PaginationState.Allow
        category_content_swipe_refresh.isRefreshing = true
        category_content_swipe_refresh.visibilityGone(true)
        layout_progress_bar.visibilityGone(false)
        layout_error.visibilityGone(false)
        error_repeat_progress_bar.visibilityGone(false)
        error_repeat_button.visibilityGone(false)
        adapter.swapData(data)
    }

    private fun establishRefreshAfterFullDataState(data: List<IComparableItem>) {
        scrollListener.state = LinearInfiniteScrollListener.PaginationState.Allow
        category_content_swipe_refresh.isRefreshing = true
        category_content_swipe_refresh.visibilityGone(true)
        layout_progress_bar.visibilityGone(false)
        layout_error.visibilityGone(false)
        error_repeat_progress_bar.visibilityGone(false)
        error_repeat_button.visibilityGone(false)
        adapter.swapData(data)
    }

    companion object {
        private const val CATEGORY = "CATEGORY"

        fun newInstance(category: CategoryVM) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CATEGORY, category)
                }
            }
    }
}