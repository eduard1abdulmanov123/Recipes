package abdulmanov.eduard.recipes.presentation.ui.mapper

import abdulmanov.eduard.recipes.domain.models.Category
import abdulmanov.eduard.recipes.domain.models.CategoryWithRecipes
import abdulmanov.eduard.recipes.domain.models.Recipe
import abdulmanov.eduard.recipes.domain.models.Tape
import abdulmanov.eduard.recipes.presentation.ui.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesViewModelMapperImpl @Inject constructor():RecipesViewModelMapper {

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

    override fun mapTapeToViewModel(tape: Tape): TapeViewModel {
        return TapeViewModel(
            BestRecipesViewModel(mapRecipesToViewModels(tape.bestRecipes)),
            tape.recipesByCategory.map(this::mapCategoryWithRecipesToViewModel)
        )
    }

    private fun mapCategoryToViewModel(category: Category): CategoryViewModel {
        return CategoryViewModel(category.name,category.value)
    }

    private fun mapCategoryWithRecipesToViewModel(categoryWithRecipes: CategoryWithRecipes): CategoryWithRecipesViewModel {
        return CategoryWithRecipesViewModel(
            mapCategoryToViewModel(categoryWithRecipes.category),
            mapRecipesToViewModels(categoryWithRecipes.recipes)
        )
    }
}