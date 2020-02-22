package abdulmanov.eduard.recipes.presentation.ui.mapper

import abdulmanov.eduard.recipes.domain.models.Recipe
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeViewModel

class RecipesViewModelMapperImpl:RecipesViewModelMapper {

    override fun mapRecipesToViewModels(recipes: List<Recipe>): List<RecipeViewModel> {
        return recipes.map {
            RecipeViewModel(
                it.id,
                it.name,
                it.image,
                it.countIngredient,
                it.countPortion,
                it.time,
                it.countLike,
                it.countDislike
            )
        }
    }

}