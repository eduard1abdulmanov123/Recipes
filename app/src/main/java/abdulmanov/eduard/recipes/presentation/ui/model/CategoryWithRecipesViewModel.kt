package abdulmanov.eduard.recipes.presentation.ui.model

import abdulmanov.eduard.recipes.presentation.ui.adapters.IItem

data class CategoryWithRecipesViewModel(
    val category:CategoryViewModel,
    val recipes:List<RecipeViewModel>
): IItem