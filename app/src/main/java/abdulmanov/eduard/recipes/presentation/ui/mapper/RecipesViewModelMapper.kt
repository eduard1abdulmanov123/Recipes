package abdulmanov.eduard.recipes.presentation.ui.mapper


import abdulmanov.eduard.recipes.domain.models.Recipe
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeViewModel

interface RecipesViewModelMapper {

    fun mapRecipesToViewModels(recipes:List<Recipe>):List<RecipeViewModel>

}