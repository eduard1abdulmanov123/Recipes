package abdulmanov.eduard.recipes.domain.interactors.recipes

import abdulmanov.eduard.recipes.domain.interactors.base.SingleUseCase
import abdulmanov.eduard.recipes.domain.models.Tape
import abdulmanov.eduard.recipes.domain.repositories.RecipesRepository
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class GetTapeUseCase(
    private val recipesRepository: RecipesRepository
) : SingleUseCase<Tape> {

    override fun execute(): Single<Tape> {
        return Single.zip(
            recipesRepository.getBestRecipesOfTheDay(),
            recipesRepository.getCategories(),
            BiFunction { bestRecipes, recipesByCategory ->
                Tape(bestRecipes, recipesByCategory)
            }
        )
    }
}