package abdulmanov.eduard.recipes.presentation.ui.recipes.adapters

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.common.getScreenSize
import abdulmanov.eduard.recipes.presentation.common.inflate
import abdulmanov.eduard.recipes.presentation.common.loadImg
import abdulmanov.eduard.recipes.presentation.ui.base.BaseAdapter
import abdulmanov.eduard.recipes.presentation.ui.recipes.models.RecipePresentationModel
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_list_best_recipe.view.*

class BestRecipesAdapter(
    private val itemViewClickListener: (RecipePresentationModel) -> Unit?
) : BaseAdapter<RecipePresentationModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<RecipePresentationModel> {
        return ViewHolder(parent.inflate(R.layout.item_list_best_recipe))
    }

    inner class ViewHolder(view: View) : BaseAdapter.BaseViewHolder<RecipePresentationModel>(view) {

        init {
            initSize()
            initItemViewClickListener()
        }

        override fun bind(model: RecipePresentationModel, position: Int) {
            itemView.run {
                if (model.image.isNotEmpty()) {
                    bestRecipeImage.loadImg(model.image, R.color.color_placeholder)
                } else {
                    bestRecipeImage.loadImg(
                        R.drawable.placeholder,
                        R.color.color_placeholder
                    )
                }
                bestRecipeName.text = model.name
                bestRecipeCountLike.text = model.countLike
                bestRecipeTime.text = model.time
            }
        }

        private fun initSize() {
            itemView.bestRecipeImage.run {
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