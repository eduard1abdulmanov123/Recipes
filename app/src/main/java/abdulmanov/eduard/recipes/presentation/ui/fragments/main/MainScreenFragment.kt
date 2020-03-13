package abdulmanov.eduard.recipes.presentation.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.app.BaseApp
import abdulmanov.eduard.recipes.presentation.common.visibilityGone
import abdulmanov.eduard.recipes.presentation.ui.activities.MainActivity
import abdulmanov.eduard.recipes.presentation.ui.adapters.*
import abdulmanov.eduard.recipes.presentation.ui.base.*
import abdulmanov.eduard.recipes.presentation.ui.fragments.category.CategoryScreenFragment
import abdulmanov.eduard.recipes.presentation.ui.model.TapeViewModel
import android.content.Context
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main_screen.*
import kotlinx.android.synthetic.main.fragment_main_screen.main_screen_toolbar
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_progress_bar.*
import javax.inject.Inject

class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainScreenViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BaseApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()

        viewModel = ViewModelProvider(this,viewModelFactory).get(MainScreenViewModel::class.java)
        viewModel.state.observe(viewLifecycleOwner, Observer(this::updateState))

        if(savedInstanceState == null){
            viewModel.loadTape()
        }
    }

    private fun initUI(){
        initToolbar()
        initBestRecipesRecyclerView()
        initCategoriesRecyclerView()
        initErrorRepeatButton()
    }

    private fun initToolbar(){
        main_screen_toolbar.run {
            setTitle(R.string.tape_title)
        }
    }

    private fun initBestRecipesRecyclerView(){
        main_screen_best_recipes_recycler_view.run {
            setHasFixedSize(true)
            addItemDecoration(HorizontalItemDecoration(6,4,context))
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = BestRecipesAdapter{

            }
        }
    }

    private fun initCategoriesRecyclerView(){
        main_screen_categories_recycler_view.run {
            setHasFixedSize(true)
            addItemDecoration(GridItemDecoration(6, 6, context))
            layoutManager = GridLayoutManager(context, 2)
            adapter = CategoriesAdapter{
                (activity as MainActivity).startFragment(CategoryScreenFragment.newInstance(it.value))
            }
        }
    }

    private fun initErrorRepeatButton(){
        error_repeat_button.setOnClickListener {
            viewModel.repeat()
        }
    }

    private fun updateState(state:ScreenState) {
        when(state){
            is ScreenState.StartState -> establishStartState()
            is ScreenState.ProgressState -> establishProgressState()
            is ScreenState.DataState<*> -> establishDataState(state.data as TapeViewModel)
            is ScreenState.ErrorState -> establishErrorState(state.message)
            is ScreenState.ProgressAfterErrorState -> establishProgressAfterErrorState()
        }
    }

    private fun establishStartState(){
        main_screen_content.visibilityGone(false)
        layout_error.visibilityGone(false)
        layout_progress_bar.visibilityGone(false)
    }

    private fun establishProgressState(){
        main_screen_content.visibilityGone(false)
        layout_error.visibilityGone(false)
        layout_progress_bar.visibilityGone(true)
    }

    private fun establishDataState(tapeViewModel: TapeViewModel){
        layout_error.visibilityGone(false)
        layout_progress_bar.visibilityGone(false)
        main_screen_content.visibilityGone(true)
        (main_screen_best_recipes_recycler_view.adapter as BestRecipesAdapter).updateItems(tapeViewModel.bestRecipes)
        (main_screen_categories_recycler_view.adapter as CategoriesAdapter).updateItems(tapeViewModel.categories)
    }

    private fun establishErrorState(message:Int){
        main_screen_content.visibilityGone(false)
        layout_progress_bar.visibilityGone(false)
        layout_error.visibilityGone(true)
        error_repeat_button.visibilityGone(true)
        error_repeat_progress_bar.visibilityGone(false)
        error_secondary_message.setText(message)
    }

    private fun establishProgressAfterErrorState(){
        main_screen_content.visibilityGone(false)
        layout_progress_bar.visibilityGone(false)
        layout_error.visibilityGone(true)
        error_repeat_button.visibilityGone(false)
        error_repeat_progress_bar.visibilityGone(true)
    }
}
