package abdulmanov.eduard.recipes.ui.model

data class RecipeViewModel(
    val id:Long,
    val name:String,
    val image:String,
    val countIngredient:String,
    val countPortion:String,
    val time:String,
    val countLike:Long,
    val countDislike:Long
)