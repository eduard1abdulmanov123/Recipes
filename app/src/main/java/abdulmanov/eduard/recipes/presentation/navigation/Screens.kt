package abdulmanov.eduard.recipes.presentation.navigation

import abdulmanov.eduard.recipes.presentation.ui.fragments.category.CategoryFragment
import abdulmanov.eduard.recipes.presentation.ui.fragments.container.RecipesContainerFragment
import abdulmanov.eduard.recipes.presentation.ui.fragments.tape.TapeFragment
import abdulmanov.eduard.recipes.presentation.ui.model.CategoryVM
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    object RecipesContainer: SupportAppScreen(){
        override fun getFragment() = RecipesContainerFragment()
    }

    object MainScreen:SupportAppScreen(){
        override fun getFragment() = TapeFragment()
    }

    data class Category(
        val category:CategoryVM
    ):SupportAppScreen(){
        override fun getFragment() = CategoryFragment.newInstance(category)
    }
}