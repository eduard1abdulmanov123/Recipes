package abdulmanov.eduard.recipes.presentation.navigation

import abdulmanov.eduard.recipes.presentation.ui.fragments.category.CategoryFragment
import abdulmanov.eduard.recipes.presentation.ui.fragments.details.DetailsRecipeFragment
import abdulmanov.eduard.recipes.presentation.ui.fragments.tape.TapeFragment
import abdulmanov.eduard.recipes.presentation.ui.models.CategoryPresentationModel
import abdulmanov.eduard.recipes.presentation.ui.models.RecipePresentationModel
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object Tape:SupportAppScreen(){
        override fun getFragment() = TapeFragment()
    }

    data class Category(val category:CategoryPresentationModel):SupportAppScreen(){
        override fun getFragment() = CategoryFragment.newInstance(category)
    }

    data class DetailsRecipe(val recipe:RecipePresentationModel):SupportAppScreen(){
        override fun getFragment() = DetailsRecipeFragment.newInstance(recipe)
    }
}