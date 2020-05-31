package abdulmanov.eduard.recipes.presentation.ui.mappers


import abdulmanov.eduard.recipes.domain.models.Recipe
import abdulmanov.eduard.recipes.domain.models.Tape
import abdulmanov.eduard.recipes.presentation.ui.models.RecipePresentationModel
import abdulmanov.eduard.recipes.presentation.ui.models.TapePresentationModel

interface RecipesPresentationModelMapper {

    fun mapRecipesToPresentationModel(recipes:List<Recipe>):List<RecipePresentationModel>

    fun mapTapeToPresentationModel(tape:Tape):TapePresentationModel
}