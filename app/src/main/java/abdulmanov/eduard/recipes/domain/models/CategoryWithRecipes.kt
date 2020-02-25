package abdulmanov.eduard.recipes.domain.models

data class CategoryWithRecipes(
    val category: Category,
    val recipes:List<Recipe>
)