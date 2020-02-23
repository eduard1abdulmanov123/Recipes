package abdulmanov.eduard.recipes.presentation.ui.adapters

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.common.loadImg
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeViewModel
import com.example.delegateadapter.delegate.KDelegateAdapter
import kotlinx.android.synthetic.main.item_list_recipe.*

class RecipesDelegateAdapter (
    private val itemViewClickListener:(RecipeViewModel)->Unit
) : KDelegateAdapter<RecipeViewModel>(){

    override fun getLayoutId() = R.layout.item_list_recipe

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is RecipeViewModel
    }

    override fun onBind(item: RecipeViewModel, viewHolder: KViewHolder) {
        viewHolder.run {
            itemView.setOnClickListener { itemViewClickListener.invoke(item) }
            if(item.image.isNotEmpty()) item_list_recipe_image.loadImg(item.image)
            item_list_recipe_name.text = item.name
            item_list_recipe_count_ingredients.text = item.countIngredient
            item_list_recipe_count_portions.text = item.countPortion
            item_list_recipe_time.text = item.time
            item_list_recipe_thumb_up.text = item.countLike
            item_list_recipe_thumb_down.text = item.countDislike
        }
    }
}