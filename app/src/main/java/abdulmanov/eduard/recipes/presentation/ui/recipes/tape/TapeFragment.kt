package abdulmanov.eduard.recipes.presentation.ui.recipes.tape

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.app.BaseApp
import abdulmanov.eduard.recipes.presentation.navigation.BackButtonListener
import abdulmanov.eduard.recipes.presentation.navigation.RouterProvide
import abdulmanov.eduard.recipes.presentation.ui.recipes.adapters.*
import abdulmanov.eduard.recipes.presentation.ui.base.*
import abdulmanov.eduard.recipes.presentation.ui.recipes.models.TapePresentationModel
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
        mainScreenToolbar.setTitle(R.string.tape_title)

        mainScreenBestRecipesRecyclerView.run {
            setHasFixedSize(true)
            addItemDecoration(HorizontalItemDecoration(6, 4, context))
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = BestRecipesAdapter(viewModel::onClickBestRecipeItem)
        }

        mainScreenCategoriesRecyclerView.run {
            setHasFixedSize(true)
            addItemDecoration(GridItemDecoration(6, 6, context))
            layoutManager = GridLayoutManager(context, 2)
            adapter = CategoriesAdapter(viewModel::onClickCategoryItem)
        }

        errorRepeatButton.setOnClickListener {
            viewModel.repeat()
        }
    }

    private fun updateState(state: ScreenState) {
        mainScreenContent.visibility = View.GONE
        layoutProgressBar.visibility = View.GONE
        layoutError.visibility = View.GONE
        errorRepeatButton.visibility = View.GONE
        errorRepeatProgressBar.visibility = View.GONE

        when (state) {
            is ScreenState.Progress -> layoutProgressBar.visibility = View.VISIBLE

            is ScreenState.Data<*> -> {
                val tape = state.data as TapePresentationModel
                mainScreenContent.visibility = View.VISIBLE
                (mainScreenBestRecipesRecyclerView.adapter as BestRecipesAdapter).updateItems(tape.bestRecipes)
                (mainScreenCategoriesRecyclerView.adapter as CategoriesAdapter).updateItems(tape.categories)
            }

            is ScreenState.Error -> {
                layoutError.visibility = View.VISIBLE
                errorRepeatButton.visibility = View.VISIBLE
                errorSecondaryMessage.setText(state.message)
            }

            is ScreenState.ProgressAfterError -> {
                layoutError.visibility = View.VISIBLE
                errorRepeatProgressBar.visibility = View.VISIBLE
            }
        }
    }
}