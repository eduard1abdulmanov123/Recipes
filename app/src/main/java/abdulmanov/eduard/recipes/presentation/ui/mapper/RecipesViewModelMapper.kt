package abdulmanov.eduard.recipes.presentation.ui.mapper


import abdulmanov.eduard.recipes.domain.models.Recipe
import abdulmanov.eduard.recipes.domain.models.Tape
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeViewModel
import abdulmanov.eduard.recipes.presentation.ui.model.TapeViewModel

interface RecipesViewModelMapper {

    fun mapRecipesToViewModels(recipes:List<Recipe>):List<RecipeViewModel>

    fun mapTapeToViewModel(tape:Tape):TapeViewModel

}