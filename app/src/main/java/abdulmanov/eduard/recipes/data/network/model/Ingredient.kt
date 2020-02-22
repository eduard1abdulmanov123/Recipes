package abdulmanov.eduard.recipes.data.network.model

data class Ingredient(
    val id:Long,
    val name:String,
    val amountInPortions: List<String> = listOf()
)
