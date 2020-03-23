package abdulmanov.eduard.recipes.presentation.ui.fragments.tape

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.app.BaseApp
import abdulmanov.eduard.recipes.presentation.common.visibilityGone
import abdulmanov.eduard.recipes.presentation.navigation.BackButtonListener
import abdulmanov.eduard.recipes.presentation.navigation.RouterProvide
import abdulmanov.eduard.recipes.presentation.ui.adapters.*
import abdulmanov.eduard.recipes.presentation.ui.base.*
import abdulmanov.eduard.recipes.presentation.ui.model.TapeVM
import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_tape.*
import kotlinx.android.synthetic.main.fragment_tape.main_screen_toolbar
import kotlinx.android.synthetic.main.layout_error.*
import kotlinx.android.synthetic.main.layout_progress_bar.*
import javax.inject.Inject

class TapeFragment : Fragment(R.layout.fragment_tape), BackButtonListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: TapeViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BaseApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(TapeViewModel::class.java)
            .apply {
                router = (parentFragment as RouterProvide).getRouter()

                state.observe(
                    viewLifecycleOwner,
                    Observer(this@TapeFragment::updateState)
                )
            }

        initUI()

        if (savedInstanceState == null) {
            viewModel.loadTape()
        }
    }

    override fun onBackPressed(): Boolean {
        viewModel.onBackPressed()
        return true
    }

    private fun initUI() {
        initToolbar()
        initBestRecipesRecyclerView()
        initCategoriesRecyclerView()
        initErrorRepeatButton()
    }

    private fun initToolbar() {
        main_screen_toolbar.run {
            setTitle(R.string.tape_title)
        }
    }

    private fun initBestRecipesRecyclerView() {
        main_screen_best_recipes_recycler_view.run {
            setHasFixedSize(true)
            addItemDecoration(HorizontalItemDecoration(6, 4, context))
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = BestRecipesAdapter {

            }
        }
    }

    private fun initCategoriesRecyclerView() {
        main_screen_categories_recycler_view.run {
            setHasFixedSize(true)
            addItemDecoration(GridItemDecoration(6, 6, context))
            layoutManager = GridLayoutManager(context, 2)
            adapter = CategoriesAdapter(viewModel::onClickCategoryItem)
        }
    }

    private fun initErrorRepeatButton() {
        error_repeat_button.setOnClickListener {
            viewModel.repeat()
        }
    }

    private fun updateState(state: ScreenState) {
        when (state) {
            is ScreenState.Start -> establishStartState()
            is ScreenState.Progress -> establishProgressState()
            is ScreenState.Data<*> -> establishDataState(state.data as TapeVM)
            is ScreenState.Error -> establishErrorState(state.message)
            is ScreenState.ProgressAfterError -> establishProgressAfterErrorState()
        }
    }

    private fun establishStartState() {
        main_screen_content.visibilityGone(false)
        layout_error.visibilityGone(false)
        layout_progress_bar.visibilityGone(false)
    }

    private fun establishProgressState() {
        main_screen_content.visibilityGone(false)
        layout_error.visibilityGone(false)
        layout_progress_bar.visibilityGone(true)
    }

    private fun establishDataState(tapeVM: TapeVM) {
        layout_error.visibilityGone(false)
        layout_progress_bar.visibilityGone(false)
        main_screen_content.visibilityGone(true)
        (main_screen_best_recipes_recycler_view.adapter as BestRecipesAdapter).updateItems(tapeVM.bestRecipes)
        (main_screen_categories_recycler_view.adapter as CategoriesAdapter).updateItems(tapeVM.categories)
    }

    private fun establishErrorState(message: Int) {
        main_screen_content.visibilityGone(false)
        layout_progress_bar.visibilityGone(false)
        layout_error.visibilityGone(true)
        error_repeat_button.visibilityGone(true)
        error_repeat_progress_bar.visibilityGone(false)
        error_secondary_message.setText(message)
    }

    private fun establishProgressAfterErrorState() {
        main_screen_content.visibilityGone(false)
        layout_progress_bar.visibilityGone(false)
        layout_error.visibilityGone(true)
        error_repeat_button.visibilityGone(false)
        error_repeat_progress_bar.visibilityGone(true)
    }
}
