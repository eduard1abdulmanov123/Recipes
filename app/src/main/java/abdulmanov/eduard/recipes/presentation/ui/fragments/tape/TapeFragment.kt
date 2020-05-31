package abdulmanov.eduard.recipes.presentation.ui.fragments.tape

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.app.BaseApp
import abdulmanov.eduard.recipes.presentation.common.visibility
import abdulmanov.eduard.recipes.presentation.navigation.BackButtonListener
import abdulmanov.eduard.recipes.presentation.navigation.RouterProvide
import abdulmanov.eduard.recipes.presentation.ui.adapters.*
import abdulmanov.eduard.recipes.presentation.ui.base.*
import abdulmanov.eduard.recipes.presentation.ui.models.TapePresentationModel
import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_tape.*
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

                state.observe(viewLifecycleOwner, Observer(this@TapeFragment::updateState))
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
        main_screen_toolbar.setTitle(R.string.tape_title)

        main_screen_best_recipes_recycler_view.run {
            setHasFixedSize(true)
            addItemDecoration(HorizontalItemDecoration(6, 4, context))
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = BestRecipesAdapter(viewModel::onClickBestRecipeItem)
        }

        main_screen_categories_recycler_view.run {
            setHasFixedSize(true)
            addItemDecoration(GridItemDecoration(6, 6, context))
            layoutManager = GridLayoutManager(context, 2)
            adapter = CategoriesAdapter(viewModel::onClickCategoryItem)
        }

        error_repeat_button.setOnClickListener {
            viewModel.repeat()
        }
    }

    private fun updateState(state: ScreenState) {
        main_screen_content.visibility = View.GONE
        layout_progress_bar.visibility = View.GONE
        layout_error.visibility = View.GONE
        error_repeat_button.visibility = View.GONE
        error_repeat_progress_bar.visibility = View.GONE

        when (state) {
            is ScreenState.Progress -> layout_progress_bar.visibility = View.VISIBLE

            is ScreenState.Data<*> -> {
                val tape = state.data as TapePresentationModel
                main_screen_content.visibility = View.VISIBLE
                (main_screen_best_recipes_recycler_view.adapter as BestRecipesAdapter).updateItems(tape.bestRecipes)
                (main_screen_categories_recycler_view.adapter as CategoriesAdapter).updateItems(tape.categories)
            }

            is ScreenState.Error -> {
                layout_error.visibility = View.VISIBLE
                error_repeat_button.visibility = View.VISIBLE
                error_secondary_message.setText(state.message)
            }

            is ScreenState.ProgressAfterError -> {
                layout_error.visibility = View.VISIBLE
                error_repeat_progress_bar.visibility = View.VISIBLE
            }
        }
    }
}