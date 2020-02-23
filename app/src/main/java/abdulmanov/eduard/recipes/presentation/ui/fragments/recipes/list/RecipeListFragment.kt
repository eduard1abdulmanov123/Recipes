package abdulmanov.eduard.recipes.presentation.ui.fragments.recipes.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.ui.base.ListState
import abdulmanov.eduard.recipes.presentation.common.visibilityGone
import abdulmanov.eduard.recipes.presentation.ui.adapters.LoadingDelegateAdapter
import abdulmanov.eduard.recipes.presentation.ui.adapters.RecipesDelegateAdapter
import abdulmanov.eduard.recipes.presentation.ui.base.LinearInfiniteScrollListener
import abdulmanov.eduard.recipes.presentation.ui.model.CategoryViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import com.example.delegateadapter.delegate.diff.IComparableItem
import kotlinx.android.synthetic.main.fragment_recipes.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_progress_bar.*

class RecipeListFragment : Fragment() {

    companion object{
        private const val CATEGORY = "CATEGORY"

        fun newInstance(category:CategoryViewModel):RecipeListFragment{
            return RecipeListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CATEGORY,category)
                }
            }
        }
    }

    private lateinit var category:CategoryViewModel

    private lateinit var viewModel:RecipeListViewModel

    private lateinit var scrollListener:LinearInfiniteScrollListener
    private lateinit var adapter:DiffUtilCompositeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getParcelable(CATEGORY)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        viewModel = ViewModelProvider(this).get(RecipeListViewModel::class.java)
        viewModel.state.observe(viewLifecycleOwner, Observer(this::updateState))
        viewModel.data.observe(viewLifecycleOwner, Observer(this::updateItems))
    }

    private fun initUI(){
        iniToolbar()
        initSwipeRefreshLayout()
        initRecyclerView()
        initRefreshButton()
    }

    private fun iniToolbar(){
        recipes_toolbar.run {
            title = category.name
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun initRecyclerView(){
        val linearLayoutManager = LinearLayoutManager(context)
        initScrollListener(linearLayoutManager)
        initAdapter()
        recipes_recycler_view.run {
            setHasFixedSize(true)
            addOnScrollListener(this@RecipeListFragment.scrollListener)
            itemAnimator = null
            layoutManager = linearLayoutManager
            adapter = this@RecipeListFragment.adapter
        }
    }

    private fun initScrollListener(layoutManager: LinearLayoutManager){
        scrollListener = LinearInfiniteScrollListener(layoutManager,1){
            viewModel.loadNewPage()
        }
    }

    private fun initAdapter(){
        adapter = DiffUtilCompositeAdapter.Builder()
            .add(RecipesDelegateAdapter{

            })
            .add(LoadingDelegateAdapter{
                viewModel.refresh()
            })
            .build()
    }

    private fun initSwipeRefreshLayout(){
        recipes_content.setOnRefreshListener {
            viewModel.restart()
        }
    }

    private fun initRefreshButton(){
        error_refresh_button.setOnClickListener {
            viewModel.refresh()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun updateState(state: ListState){
        when(state){
            is ListState.EmptyState->{
                layout_error.visibilityGone(false)
                layout_progress_bar.visibilityGone(false)
                recipes_content.visibilityGone(false)
            }
            is ListState.EmptyProgressState->{
                /*Если swipeRefresh == true, то мы показывваем уже загруженные данные,
                * а сверху крутиться спинер, иначе мы показываем прогресс бар начальной загрузки*/
                layout_progress_bar.visibilityGone(!state.swipeRefresh)
                recipes_content.visibilityGone(state.swipeRefresh)
                layout_error.visibilityGone(false)
            }
            is ListState.EmptyErrorState->{
                /*Задержка делается для того, чтобы избежать показа загрузки из SwipeRefresh
                * после того, как мы перешли в состояние Data. Примерно 100мс убирается загрузка*/
                error_refresh_progress_bar.visibilityGone(false)
                layout_progress_bar.visibilityGone(false)
                recipes_content.isRefreshing = false
                recipes_content.postDelayed({
                    recipes_content.visibilityGone(false)
                    layout_error.visibilityGone(true)
                    error_refresh_button.visibilityGone(true)
                    error_secondary_message.setText(state.message)
                },100)
            }
            is ListState.EmptyErrorStateRefresh->{
                layout_progress_bar.visibilityGone(false)
                recipes_content.visibilityGone(false)
                error_refresh_button.visibilityGone(false)
                layout_error.visibilityGone(true)
                error_refresh_progress_bar.visibilityGone(true)
            }
            is ListState.DataState->{
                layout_progress_bar.visibilityGone(false)
                layout_error.visibilityGone(false)
                recipes_content.run {
                    if(isRefreshing)
                        isRefreshing = false
                    visibilityGone(true)
                }
            }
            is ListState.PageProgressState->{}
            is ListState.PageErrorState->{}
            is ListState.AllDataState->{

            }
        }
    }

    private fun updateItems(recipes:List<IComparableItem>){
        (recipes_recycler_view.adapter as DiffUtilCompositeAdapter ).swapData(recipes)
    }

}
