package abdulmanov.eduard.recipes.presentation.ui.fragments.category

import android.os.Bundle
import androidx.fragment.app.Fragment

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.app.BaseApp
import abdulmanov.eduard.recipes.presentation.common.visibilityGone
import abdulmanov.eduard.recipes.presentation.ui.adapters.RecipesAdapter
import abdulmanov.eduard.recipes.presentation.ui.base.ScreenListState
import abdulmanov.eduard.recipes.presentation.ui.base.paginator.Paginator
import abdulmanov.eduard.recipes.presentation.ui.base.VerticalItemDecoration
import abdulmanov.eduard.recipes.presentation.ui.base.ViewModelFactory
import abdulmanov.eduard.recipes.presentation.ui.base.paginator.State
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeViewModel
import android.content.Context
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BaseApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

        viewModel = ViewModelProvider(this,viewModelFactory).get(CategoryScreenViewModel::class.java)
        viewModel.state.observe(viewLifecycleOwner, Observer(this::updateState))
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

        }
    }

    private fun initCategoryRecyclerView(){
        category_recycler_view.run {
            setHasFixedSize(true)
            addItemDecoration(VerticalItemDecoration(16,8,requireContext()))
            layoutManager = LinearLayoutManager(context)
            adapter = RecipesAdapter{

            }
        }
    }

    private fun initErrorRepeatButton(){
        error_repeat_button.setOnClickListener {
            viewModel.refresh()
        }
    }

    private fun updateState(state: ScreenListState) {
        when(state){
            is ScreenListState.StartProgressState -> {
                category_content_swipe_refresh.visibilityGone(false)
                layout_error.visibilityGone(false)
                layout_progress_bar.visibilityGone(true)
            }
            is ScreenListState.StartErrorState -> {
                category_content_swipe_refresh.visibilityGone(false)
                layout_progress_bar.visibilityGone(false)
                layout_error.visibilityGone(true)
                error_repeat_button.visibilityGone(true)
                error_repeat_progress_bar.visibilityGone(false)
                error_secondary_message.setText(state.message)
            }
            is ScreenListState.StartProgressAfterErrorState -> {
                category_content_swipe_refresh.visibilityGone(false)
                layout_progress_bar.visibilityGone(false)
                layout_error.visibilityGone(true)
                error_repeat_progress_bar.visibilityGone(true)
                error_repeat_button.visibilityGone(false)
            }
            is ScreenListState.DataState<*> -> {
                layout_error.visibilityGone(false)
                layout_progress_bar.visibilityGone(false)
                category_content_swipe_refresh.visibilityGone(true)
                (category_recycler_view.adapter as RecipesAdapter).updateItems(state.data as List<RecipeViewModel>)
            }
        }
    }
}
