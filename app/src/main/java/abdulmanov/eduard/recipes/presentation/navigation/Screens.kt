package abdulmanov.eduard.recipes.presentation.navigation

import abdulmanov.eduard.recipes.presentation.ui.recipes.category.CategoryFragment
import abdulmanov.eduard.recipes.presentation.ui.recipes.details.DetailsRecipeFragment
import abdulmanov.eduard.recipes.presentation.ui.recipes.tape.TapeFragment
import abdulmanov.eduard.recipes.presentation.ui.recipes.models.CategoryPresentationModel
import abdulmanov.eduard.recipes.presentation.ui.recipes.models.RecipePresentationModel
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object Tape : SupportAppScreen() {
        override fun getFragment() = TapeFragment()
    }

    data class Category(val category: CategoryPresentationModel) : SupportAppScreen() {
        override fun getFragment() = CategoryFragment.newInstance(category)
    }

    data class DetailsRecipe(val recipe: RecipePresentationModel) : SupportAppScreen() {
        override fun getFragment() = DetailsRecipeFragment.newInstance(recipe)
    }
}