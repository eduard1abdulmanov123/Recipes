package abdulmanov.eduard.recipes.presentation.ui.adapters

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.common.getScreenSize
import abdulmanov.eduard.recipes.presentation.common.inflate
import abdulmanov.eduard.recipes.presentation.common.loadImg
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeViewModel
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_list_best_recipe.view.*

class BestRecipesAdapter(
    private val itemViewClickListener:(RecipeViewModel)->Unit
):BaseAdapter<RecipeViewModel>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<RecipeViewModel> {
        return ViewHolder(parent.inflate(R.layout.item_list_best_recipe))
    }

    inner class ViewHolder(view: View) : BaseAdapter.BaseViewHolder<RecipeViewModel>(view) {

        init {
            initSize()
            initItemViewClickListener()
        }

        override fun bind(model: RecipeViewModel, position: Int) {
            itemView.run {
                if (model.image.isNotEmpty()) {
                    best_recipe_image.loadImg(model.image, R.color.color_placeholder)
                } else {
                    best_recipe_image.loadImg(
                        R.drawable.placeholder,
                        R.color.color_placeholder
                    )
                }
                best_recipe_name.text = model.name
                best_recipe_count_like.text = model.countLike
                best_recipe_time.text = model.time
            }
        }

        private fun initSize() {
            itemView.best_recipe_image.run {
                val size = context.getScreenSize()
                layoutParams.width = (size.x * 0.9).toInt()
                layoutParams.height = (size.y * 0.3).toInt()
            }
        }

        private fun initItemViewClickListener() {
            itemView.setOnClickListener {
                itemViewClickListener.invoke(dataList[adapterPosition])
            }
        }

    }
}