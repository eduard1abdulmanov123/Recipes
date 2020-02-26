package abdulmanov.eduard.recipes.presentation.ui.adapters

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.common.getScreenSize
import abdulmanov.eduard.recipes.presentation.common.inflate
import abdulmanov.eduard.recipes.presentation.common.loadImg
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeViewModel
import android.util.Log
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_list_best_recipe.view.*

class BestRecipesAdapter:BaseAdapter<RecipeViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<RecipeViewModel> {
        return ViewHolder(parent.inflate(R.layout.item_list_best_recipe))
    }

    inner class ViewHolder(view: View):BaseAdapter.BaseViewHolder<RecipeViewModel>(view){

        init{
            itemView.item_list_best_recipe_image.run {
                val size = context.getScreenSize()
                layoutParams.width = (size.x*0.8).toInt()
                layoutParams.height = (size.y*0.25).toInt()
                Log.d("BestRecipesAdapter","${layoutParams.width} ${layoutParams.height}")
            }
        }

        override fun bind(model: RecipeViewModel, position: Int) {
            itemView.run {
                if(model.image.isNotEmpty()) {
                    item_list_best_recipe_image.loadImg(model.image, R.color.color_placeholder)
                }else{
                    item_list_best_recipe_image.loadImg(R.drawable.placeholder, R.color.color_placeholder)
                }
                item_list_best_recipe_name.text = model.name
                item_list_best_recipe_count_like.text = model.countLike
                item_list_best_recipe_time.text = model.time
            }
        }

    }
}