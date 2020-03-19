package abdulmanov.eduard.recipes.presentation.ui.fragments.category

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.app.BaseApp
import abdulmanov.eduard.recipes.presentation.common.handleError
import abdulmanov.eduard.recipes.presentation.common.visibilityGone
import abdulmanov.eduard.recipes.presentation.ui.adapters.LoadingDelegateAdapter
import abdulmanov.eduard.recipes.presentation.ui.adapters.RecipesDelegateAdapter
import abdulmanov.eduard.recipes.presentation.ui.base.*
import abdulmanov.eduard.recipes.presentation.ui.model.CategoryViewModel
import abdulmanov.eduard.recipes.presentation.ui.model.LoadingViewModel
import abdulmanov.eduard.recipes.presentation.ui.model.LoadingViewModel.*
import android.content.Context
import android.os.Bundle
import android.util.Log
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

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: CategoryScreenViewModel

    private lateinit var category: CategoryViewModel

    private lateinit var adapter: DiffUtilCompositeAdapter
    private lateinit var scrollListener: LinearInfiniteScrollListener
    private var loadingItem = LoadingViewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BaseApp).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = requireArguments().getParcelable(CATEGORY)!!
        Log.d("CategoryScreenViewModel",category.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CategoryScreenViewModel::class.java)
            .apply {
                category = this@CategoryScreenFragment.category.value
                state.observe(
                    viewLifecycleOwner,
                    Observer(this@CategoryScreenFragment::updateState)
                )
                message.observe(
                    viewLifecycleOwner,
                    Observer(this@CategoryScreenFragment::showMessage)
                )
            }
        if(savedInstanceState == null)
            viewModel.refresh()
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
                //TODO
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
            adapter = this@CategoryScreenFragment.adapter
        }
    }

    private fun initScrollListener(linearLayoutManager: LinearLayoutManager) {
        scrollListener = LinearInfiniteScrollListener(linearLayoutManager, 0) {
            viewModel.loadNextPage()
        }
    }

    private fun initAdapter() {
        adapter = DiffUtilCompositeAdapter.Builder()
            .add(RecipesDelegateAdapter {

            })
            .add(LoadingDelegateAdapter {
                viewModel.repeat()
            })
            .build()
    }

    private fun initErrorRepeatButton() {
        error_repeat_button.setOnClickListener {
            viewModel.repeat()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun updateState(state: Paginator.State) {
        when (state) {
            is Paginator.State.Empty -> {
                scrollListener.state = LinearInfiniteScrollListener.PaginationState.Ban
                category_content_swipe_refresh.isRefreshing = false
                category_content_swipe_refresh.visibilityGone(false)
                layout_progress_bar.visibilityGone(false)
                layout_error.visibilityGone(false)
                error_repeat_progress_bar.visibilityGone(false)
                error_repeat_button.visibilityGone(false)
            }
            is Paginator.State.EmptyProgress -> {
                scrollListener.state = LinearInfiniteScrollListener.PaginationState.Ban
                category_content_swipe_refresh.isRefreshing = false
                category_content_swipe_refresh.visibilityGone(false)
                layout_progress_bar.visibilityGone(true)
                layout_error.visibilityGone(false)
                error_repeat_progress_bar.visibilityGone(false)
                error_repeat_button.visibilityGone(false)
            }
            is Paginator.State.EmptyError -> {
                scrollListener.state = LinearInfiniteScrollListener.PaginationState.Ban
                category_content_swipe_refresh.isRefreshing = false
                category_content_swipe_refresh.visibilityGone(false)
                layout_progress_bar.visibilityGone(false)
                layout_error.visibilityGone(true)
                error_repeat_progress_bar.visibilityGone(false)
                error_repeat_button.visibilityGone(true)
                error_secondary_message.setText(state.error.handleError())
            }
            is Paginator.State.EmptyProgressAfterError -> {
                scrollListener.state = LinearInfiniteScrollListener.PaginationState.Ban
                category_content_swipe_refresh.isRefreshing = false
                category_content_swipe_refresh.visibilityGone(false)
                layout_progress_bar.visibilityGone(false)
                layout_error.visibilityGone(true)
                error_repeat_progress_bar.visibilityGone(true)
                error_repeat_button.visibilityGone(false)
                error_secondary_message.setText(state.error.handleError())
            }
            is Paginator.State.Data<*> -> {
                scrollListener.state = LinearInfiniteScrollListener.PaginationState.Allow
                category_content_swipe_refresh.isRefreshing = false
                category_content_swipe_refresh.visibilityGone(true)
                layout_progress_bar.visibilityGone(false)
                layout_error.visibilityGone(false)
                error_repeat_progress_bar.visibilityGone(false)
                error_repeat_button.visibilityGone(false)
                adapter.swapData(state.data as List<IComparableItem>)
            }
            is Paginator.State.NewPageProgress<*> -> {
                scrollListener.state = LinearInfiniteScrollListener.PaginationState.Ban
                category_content_swipe_refresh.isRefreshing = false
                category_content_swipe_refresh.visibilityGone(true)
                layout_progress_bar.visibilityGone(false)
                layout_error.visibilityGone(false)
                error_repeat_progress_bar.visibilityGone(false)
                error_repeat_button.visibilityGone(false)
                loadingItem = loadingItem.copy(state = LoadingViewModelState.Loading)
                adapter.swapData((state.data as List<IComparableItem>) + loadingItem)
            }
            is Paginator.State.NewPageError<*> -> {
                scrollListener.state = LinearInfiniteScrollListener.PaginationState.Ban
                category_content_swipe_refresh.isRefreshing = false
                category_content_swipe_refresh.visibilityGone(true)
                layout_progress_bar.visibilityGone(false)
                layout_error.visibilityGone(false)
                error_repeat_progress_bar.visibilityGone(false)
                error_repeat_button.visibilityGone(false)
                loadingItem = loadingItem.copy(state = LoadingViewModelState.Error)
                adapter.swapData((state.data as List<IComparableItem>) + loadingItem)
            }
            is Paginator.State.FullData<*> -> {
                scrollListener.state = LinearInfiniteScrollListener.PaginationState.Ban
                category_content_swipe_refresh.isRefreshing = false
                category_content_swipe_refresh.visibilityGone(true)
                layout_progress_bar.visibilityGone(false)
                layout_error.visibilityGone(false)
                error_repeat_progress_bar.visibilityGone(false)
                error_repeat_button.visibilityGone(false)
                adapter.swapData((state.data as List<IComparableItem>))
            }
            is Paginator.State.Refresh<*> -> {
                scrollListener.state = LinearInfiniteScrollListener.PaginationState.Allow
                category_content_swipe_refresh.isRefreshing = true
                category_content_swipe_refresh.visibilityGone(true)
                layout_progress_bar.visibilityGone(false)
                layout_error.visibilityGone(false)
                error_repeat_progress_bar.visibilityGone(false)
                error_repeat_button.visibilityGone(false)
                adapter.swapData(state.data as List<IComparableItem>)
            }
            is Paginator.State.RefreshAfterFullData<*> -> {
                scrollListener.state = LinearInfiniteScrollListener.PaginationState.Allow
                category_content_swipe_refresh.isRefreshing = true
                category_content_swipe_refresh.visibilityGone(true)
                layout_progress_bar.visibilityGone(false)
                layout_error.visibilityGone(false)
                error_repeat_progress_bar.visibilityGone(false)
                error_repeat_button.visibilityGone(false)
                adapter.swapData(state.data as List<IComparableItem>)
            }
        }
    }

    private fun showMessage(error: Event<Throwable>) {
        error.getContentIfNotHandled()?.let {
            Snackbar.make(category_root, it.handleError(), Snackbar.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val CATEGORY = "CATEGORY"

        fun newInstance(category: CategoryViewModel) =
            CategoryScreenFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CATEGORY, category)
                }
            }
    }
}
