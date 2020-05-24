package abdulmanov.eduard.recipes.presentation.ui.fragments.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.app.BaseApp
import abdulmanov.eduard.recipes.presentation.navigation.BackButtonListener
import abdulmanov.eduard.recipes.presentation.navigation.RouterProvide
import abdulmanov.eduard.recipes.presentation.ui.base.ScreenState
import abdulmanov.eduard.recipes.presentation.ui.base.ViewModelFactory
import abdulmanov.eduard.recipes.presentation.ui.fragments.category.CategoryViewModel
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeVM
import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class DetailsRecipeFragment : Fragment(R.layout.fragment_details_recipe),BackButtonListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: DetailsRecipeViewModel

    private lateinit var recipe:RecipeVM

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BaseApp).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipe = requireArguments().getParcelable(RECIPE)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(DetailsRecipeViewModel::class.java)
            .apply {
                router = (parentFragment as RouterProvide).getRouter()

                state.observe(
                    viewLifecycleOwner,
                    Observer(this@DetailsRecipeFragment::updateState)
                )
            }
    }

    override fun onBackPressed(): Boolean {
        viewModel.onBackPressed()
        return true
    }

    private fun updateState(state:ScreenState){

    }

    companion object {
        private const val RECIPE = "RECIPE"

        fun newInstance(recipe:RecipeVM) =
            DetailsRecipeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(RECIPE, recipe)
                }
            }
    }
}
