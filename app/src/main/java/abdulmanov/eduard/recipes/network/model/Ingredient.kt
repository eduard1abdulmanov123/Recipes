package abdulmanov.eduard.recipes.network.model

data class Ingredient(
    val id:Long,
    val name:String,
    val amountInPortions: List<String> = listOf()
)
