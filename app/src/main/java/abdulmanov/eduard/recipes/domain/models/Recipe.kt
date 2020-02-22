package abdulmanov.eduard.recipes.domain.models

data class Recipe(
    val id: Long,
    val name: String,
    val image: String,
    val countIngredient: String,
    val countPortion: String,
    val time: String,
    val countLike: String,
    val countDislike: String
)