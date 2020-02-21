package abdulmanov.eduard.recipes.network.model

data class ShortRecipe(
    val title:String,
    val countIngredients:String,
    val countPortion:String,
    val time:String,
    val countLike:String,
    val countDislike:String,
    val link:String,
    val image:String
)