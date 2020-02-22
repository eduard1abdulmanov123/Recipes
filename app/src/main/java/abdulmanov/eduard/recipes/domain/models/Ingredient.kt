package abdulmanov.eduard.recipes.domain.models

data class Ingredient(
    val id:Long,
    val name:String,
    val amountInPortions: List<String>
)