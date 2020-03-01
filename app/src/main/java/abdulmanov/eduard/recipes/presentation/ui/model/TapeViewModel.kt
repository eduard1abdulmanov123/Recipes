package abdulmanov.eduard.recipes.presentation.ui.model

data class TapeViewModel(
    val bestRecipes:BestRecipesViewModel,
    val recipesByCategory:List<CategoryWithRecipesViewModel>
)