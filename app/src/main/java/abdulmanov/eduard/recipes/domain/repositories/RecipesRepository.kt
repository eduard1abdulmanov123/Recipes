package abdulmanov.eduard.recipes.domain.repositories

import abdulmanov.eduard.recipes.domain.models.Category
import abdulmanov.eduard.recipes.domain.models.CategoryWithRecipes
import abdulmanov.eduard.recipes.domain.models.DetailsRecipe
import abdulmanov.eduard.recipes.domain.models.Recipe
import io.reactivex.Single

interface RecipesRepository {

    fun getCategories():Single<List<Category>>

    fun getRecipes(category:String,page:Int): Single<List<Recipe>>

    fun getRecipesForCategories(categories: List<Category>): Single<List<CategoryWithRecipes>>

    fun getBestRecipesOfTheDay():Single<List<Recipe>>

    fun getDetailsRecipe(id:String):Single<DetailsRecipe>

}