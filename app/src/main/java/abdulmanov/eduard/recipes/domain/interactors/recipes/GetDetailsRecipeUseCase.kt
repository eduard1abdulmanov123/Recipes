package abdulmanov.eduard.recipes.domain.interactors.recipes

import abdulmanov.eduard.recipes.domain.interactors.base.SingleUseCaseWithOneParameter
import abdulmanov.eduard.recipes.domain.models.DetailsRecipe
import abdulmanov.eduard.recipes.domain.repositories.RecipesRepository
import io.reactivex.Single

class GetDetailsRecipeUseCase(
    private val recipesRepository: RecipesRepository
):SingleUseCaseWithOneParameter<String,DetailsRecipe> {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun execute(id: String): Single<DetailsRecipe> {
        return recipesRepository.getDetailsRecipe(id)
    }

}