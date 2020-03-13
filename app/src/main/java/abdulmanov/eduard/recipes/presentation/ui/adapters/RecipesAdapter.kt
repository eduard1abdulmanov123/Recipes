package abdulmanov.eduard.recipes.presentation.ui.adapters

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.common.inflate
import abdulmanov.eduard.recipes.presentation.common.loadImg
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeViewModel
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_list_recipe.*
import kotlinx.android.synthetic.main.item_list_recipe.view.*

class RecipesAdapter(
    private val itemViewClickListener:(RecipeViewModel)->Unit
):BaseAdapter<RecipeViewModel>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<RecipeViewModel> {
        return ViewHolder(parent.inflate(R.layout.item_list_recipe))
    }

    inner class ViewHolder(view: View):BaseAdapter.BaseViewHolder<RecipeViewModel>(view){

        init {
            itemView.setOnClickListener {
                itemViewClickListener.invoke(dataList[adapterPosition])
            }
        }

        override fun bind(model: RecipeViewModel, position: Int) {
            itemView.run {
                if(model.image.isNotEmpty()) {
                    item_list_recipe_image.loadImg(model.image, R.color.color_placeholder)
                }else{
                    item_list_recipe_image.loadImg(R.drawable.placeholder, R.color.color_placeholder)
                }
                item_list_recipe_name.text = model.name
                item_list_recipe_count_ingredients.text = model.countIngredient
                item_list_recipe_count_portions.text =model.countPortion
                item_list_recipe_time.text = model.time
                item_list_recipe_thumb_up.text = model.countLike
                item_list_recipe_thumb_down.text = model.countDislike
            }
        }
    }
}