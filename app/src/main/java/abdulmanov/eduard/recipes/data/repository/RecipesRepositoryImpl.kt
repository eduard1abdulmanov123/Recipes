package abdulmanov.eduard.recipes.data.repository

import abdulmanov.eduard.recipes.data.network.CategoriesService
import abdulmanov.eduard.recipes.data.network.DetailsRecipeService
import abdulmanov.eduard.recipes.data.network.RecipesService
import abdulmanov.eduard.recipes.domain.models.Category
import abdulmanov.eduard.recipes.domain.models.DetailsRecipe
import abdulmanov.eduard.recipes.domain.models.Recipe
import abdulmanov.eduard.recipes.domain.repositories.RecipesRepository
import io.reactivex.Single

class RecipesRepositoryImpl(
    private val categoriesService:CategoriesService,
    private val recipesService: RecipesService,
    private val detailsRecipeService: DetailsRecipeService
):RecipesRepository {

    override fun getCategories(): Single<List<Category>> {
        return Single.create<List<Category>>{
            it.onSuccess(categoriesService.getCategories())
        }
    }

    override fun getRecipes(category: String, page: Int): Single<List<Recipe>> {
        return Single.create<List<Recipe>>{
            it.onSuccess(recipesService.getRecipes(category,page))
        }
    }

    override fun getBestRecipesOfTheDay(): Single<List<Recipe>> {
        return Single.create<List<Recipe>>{
            it.onSuccess(recipesService.getBestRecipesOfTheDay())
        }
    }

    override fun getDetailsRecipe(id: String): Single<DetailsRecipe> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}