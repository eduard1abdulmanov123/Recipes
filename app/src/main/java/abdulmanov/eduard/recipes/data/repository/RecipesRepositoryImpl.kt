package abdulmanov.eduard.recipes.data.repository

import abdulmanov.eduard.recipes.data.network.DetailsRecipeService
import abdulmanov.eduard.recipes.data.network.RecipesService
import abdulmanov.eduard.recipes.domain.models.DetailsRecipe
import abdulmanov.eduard.recipes.domain.models.Recipe
import abdulmanov.eduard.recipes.domain.repositories.RecipesRepository
import android.util.Log
import io.reactivex.Single

class RecipesRepositoryImpl(
    private val recipesService: RecipesService,
    private val detailsRecipeService: DetailsRecipeService
):RecipesRepository {

    override fun getRecipes(category: String, page: Int): Single<List<Recipe>> {
        return Single.create<List<Recipe>>{
            val data = recipesService.getRecipes(category,page)
            data.forEach {
                Log.d("RecipesRepositoryImpl",it.toString())
            }
            it.onSuccess(data)
        }
    }

    override fun getDetailsRecipe(id: String): Single<DetailsRecipe> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}