package abdulmanov.eduard.recipes.domain.repositories

import abdulmanov.eduard.recipes.domain.models.DetailsRecipe
import abdulmanov.eduard.recipes.domain.models.Recipe
import io.reactivex.Single

interface RecipesRepository {

    fun getRecipes(category:String,page:Int): Single<List<Recipe>>

    fun getDetailsRecipe(id:String):Single<DetailsRecipe>

}