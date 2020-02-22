package abdulmanov.eduard.recipes.presentation.ui.model

import com.example.delegateadapter.delegate.diff.IComparableItem

data class RecipeViewModel(
    val id:Long,
    val name:String,
    val image:String,
    val countIngredient:String,
    val countPortion:String,
    val time:String,
    val countLike:String,
    val countDislike:String
):IComparableItem{

    override fun id() = id

    override fun content() = this

}