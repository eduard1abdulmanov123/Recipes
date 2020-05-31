package abdulmanov.eduard.recipes.presentation.ui.recipes.mappers

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.domain.models.Category
import abdulmanov.eduard.recipes.domain.models.Recipe
import abdulmanov.eduard.recipes.domain.models.Tape
import abdulmanov.eduard.recipes.presentation.ui.recipes.models.*
import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesPresentationModelMapperImpl @Inject constructor(
    private val context:Context
):RecipesPresentationModelMapper {

    override fun mapRecipesToPresentationModel(recipes: List<Recipe>): List<RecipePresentationModel> {
        return recipes.map {
            RecipePresentationModel(
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

    override fun mapTapeToPresentationModel(tape: Tape): TapePresentationModel {
        return TapePresentationModel(
            bestRecipes = mapRecipesToPresentationModel(tape.bestRecipes),
            categories = mapCategoriesToViewModels(tape.categories)
        )
    }

    private fun mapCategoriesToViewModels(categories:List<Category>):List<CategoryPresentationModel>{
        return categories.map {
            CategoryPresentationModel(
                image = it.getDrawable(),
                name = it.name,
                value = it.value,
                countRecipes = context.resources.getQuantityString(
                    R.plurals.count_recipes,
                    it.countRecipes,
                    it.countRecipes
                )
            )
        }
    }

    private fun Category.getDrawable(): Int {
        return when (value) {
            "zagotovki" -> R.drawable.zagotovki
            "salaty" -> R.drawable.salaty
            "pasta-picca" -> R.drawable.pasta_picca
            "zakuski" -> R.drawable.zakuski
            "rizotto" -> R.drawable.rizotto
            "napitki" -> R.drawable.napitki
            "vypechka-deserty" -> R.drawable.vypechka_deserty
            "osnovnye-blyuda" -> R.drawable.osnovnye_blyuda
            "bulony" -> R.drawable.bulony
            "zavtraki" -> R.drawable.zavtraki
            "supy" -> R.drawable.supy
            "sendvichi" -> R.drawable.sendvichi
            "sousy-marinady" -> R.drawable.sousy_marinady
            else -> R.drawable.placeholder
        }
    }
}