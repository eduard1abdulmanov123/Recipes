package abdulmanov.eduard.recipes.presentation.navigation

import abdulmanov.eduard.recipes.presentation.ui.fragments.category.CategoryFragment
import abdulmanov.eduard.recipes.presentation.ui.fragments.container.RecipesContainerFragment
import abdulmanov.eduard.recipes.presentation.ui.fragments.details.DetailsRecipeFragment
import abdulmanov.eduard.recipes.presentation.ui.fragments.tape.TapeFragment
import abdulmanov.eduard.recipes.presentation.ui.model.CategoryVM
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeVM
import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object RecipesContainer: SupportAppScreen(){
        override fun getFragment() = RecipesContainerFragment()
    }

    object Tape:SupportAppScreen(){
        override fun getFragment() = TapeFragment()
    }

    data class Category(
        val category:CategoryVM
    ):SupportAppScreen(){
        override fun getFragment() = CategoryFragment.newInstance(category)
    }

    data class DetailsRecipe(
        val recipe:RecipeVM
    ):SupportAppScreen(){
        override fun getFragment() = DetailsRecipeFragment.newInstance(recipe)
    }
}