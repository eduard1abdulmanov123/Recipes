package abdulmanov.eduard.recipes.domain.interactors.recipes

import abdulmanov.eduard.recipes.domain.interactors.base.SingleUseCaseWithTwoParameter
import abdulmanov.eduard.recipes.domain.models.Recipe
import abdulmanov.eduard.recipes.domain.repositories.RecipesRepository
import io.reactivex.Single

class GetRecipesUseCase(
    private val recipesRepository: RecipesRepository
) : SingleUseCaseWithTwoParameter<String, Int, List<Recipe>> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(category: String, page: Int): Single<List<Recipe>> {
        return recipesRepository.getRecipes(category, page)
    }
}