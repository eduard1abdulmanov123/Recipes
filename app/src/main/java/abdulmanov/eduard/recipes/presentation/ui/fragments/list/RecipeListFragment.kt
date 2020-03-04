package abdulmanov.eduard.recipes.presentation.ui.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.app.BaseApp
import abdulmanov.eduard.recipes.presentation.ui.base.ListState
import abdulmanov.eduard.recipes.presentation.common.visibilityGone
import abdulmanov.eduard.recipes.presentation.ui.adapters.LoadingDelegateAdapter
import abdulmanov.eduard.recipes.presentation.ui.adapters.RecipesDelegateAdapter
import abdulmanov.eduard.recipes.presentation.ui.base.LinearInfiniteScrollListener
import abdulmanov.eduard.recipes.presentation.ui.base.VerticalItemDecoration
import abdulmanov.eduard.recipes.presentation.ui.base.ViewModelFactory
import abdulmanov.eduard.recipes.presentation.ui.model.CategoryViewModel
import android.content.Context
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.delegateadapter.delegate.diff.DiffUtilCompositeAdapter
import com.example.delegateadapter.delegate.diff.IComparableItem
import kotlinx.android.synthetic.main.fragment_recipes.*
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_progress_bar.*
import javax.inject.Inject

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

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel:RecipeListViewModel

    private lateinit var category:CategoryViewModel

    private lateinit var scrollListener:LinearInfiniteScrollListener
    private lateinit var adapter:DiffUtilCompositeAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!!.application as BaseApp).appComponent.inject(this)
    }

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
        viewModel = ViewModelProvider(this,viewModelFactory).get(RecipeListViewModel::class.java)
        Log.d("RecipeListFragment",category.value)
        viewModel.category = category.value
        viewModel.state.observe(viewLifecycleOwner, Observer(this::updateState))
        viewModel.data.observe(viewLifecycleOwner, Observer(this::updateItems))
        if(savedInstanceState == null){
            viewModel.restart()
        }
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
            addItemDecoration(VerticalItemDecoration(16,8,context!!))
            (itemAnimator as SimpleItemAnimator).addDuration = 0
            layoutManager = linearLayoutManager
            adapter = this@RecipeListFragment.adapter
        }
    }

    private fun initScrollListener(layoutManager: LinearLayoutManager){
        scrollListener = LinearInfiniteScrollListener(layoutManager,2){
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
                scrollListener.setStartState()
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
                Log.d("RecipeListFragment","isDateState")
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
        adapter.swapData(recipes)
    }

}
