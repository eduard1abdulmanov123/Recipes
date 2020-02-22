package abdulmanov.eduard.recipes.data.network.model

data class DetailsRecipe(
    val id:Long,
    val name:String,
    val video:String?,
    val description:String?,
    val energyValue: EnergyValue,
    val ingredients: List<List<Ingredient>>, // индексы это порции
    val time:String,
    val instruction:List<Step>
)