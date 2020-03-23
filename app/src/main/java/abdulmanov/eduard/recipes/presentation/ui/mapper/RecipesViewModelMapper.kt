package abdulmanov.eduard.recipes.presentation.ui.mapper


import abdulmanov.eduard.recipes.domain.models.Recipe
import abdulmanov.eduard.recipes.domain.models.Tape
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeVM
import abdulmanov.eduard.recipes.presentation.ui.model.TapeVM

interface RecipesViewModelMapper {

    fun mapRecipesToViewModels(recipes:List<Recipe>):List<RecipeVM>

    fun mapTapeToViewModels(tape:Tape):TapeVM
}