package abdulmanov.eduard.recipes.domain.models

data class Tape(
    val bestRecipes:List<Recipe>,
    val recipesByCategory:List<CategoryWithRecipes>
)