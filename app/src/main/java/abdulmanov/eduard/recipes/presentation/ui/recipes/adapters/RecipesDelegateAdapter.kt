package abdulmanov.eduard.recipes.presentation.ui.recipes.adapters

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.common.loadImg
import abdulmanov.eduard.recipes.presentation.ui.recipes.models.RecipePresentationModel
import com.example.delegateadapter.delegate.KDelegateAdapter
import kotlinx.android.synthetic.main.item_list_recipe.*

class RecipesDelegateAdapter (
    private val itemViewClickListener:(RecipePresentationModel)->Unit?
) : KDelegateAdapter<RecipePresentationModel>() {

    override fun getLayoutId() = R.layout.item_list_recipe

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is RecipePresentationModel
    }

    override fun onBind(item: RecipePresentationModel, viewHolder: KViewHolder) {
        viewHolder.run {
            itemView.setOnClickListener { itemViewClickListener.invoke(item) }
            if (item.image.isNotEmpty()) {
                itemListRecipeImage.loadImg(item.image, R.color.color_placeholder)
            } else {
                itemListRecipeImage.loadImg(R.drawable.placeholder, R.color.color_placeholder)
            }
            itemListRecipeName.text = item.name
            itemListRecipeCountIngredients.text = item.countIngredient
            itemListRecipeCountPortions.text = item.countPortion
            itemListRecipeTime.text = item.time
            itemListRecipeThumbUp.text = item.countLike
            itemListRecipeThumbDown.text = item.countDislike
        }
    }
}