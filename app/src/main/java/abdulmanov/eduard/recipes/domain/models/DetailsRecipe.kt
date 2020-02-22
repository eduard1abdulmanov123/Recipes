package abdulmanov.eduard.recipes.domain.models

data class DetailsRecipe(
    val id:Long,
    val name:String,
    val image:String,
    val video:String?,
    val photos:List<String>,
    val descriptions:String?,
    val naturals:List<Natural>,
    val ingredients:List<Ingredient>,
    val time:String,
    val steps:List<Step>
)