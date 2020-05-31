package abdulmanov.eduard.recipes.presentation.ui.recipes.adapters

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.common.getScreenSize
import abdulmanov.eduard.recipes.presentation.common.inflate
import abdulmanov.eduard.recipes.presentation.common.loadImg
import abdulmanov.eduard.recipes.presentation.ui.base.BaseAdapter
import abdulmanov.eduard.recipes.presentation.ui.recipes.models.CategoryPresentationModel
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_list_category.view.*

class CategoriesAdapter(
    private val itemViewClickListener:(CategoryPresentationModel) -> Unit?
) : BaseAdapter<CategoryPresentationModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<CategoryPresentationModel> {
        return ViewHolder(parent.inflate(R.layout.item_list_category))
    }

    inner class ViewHolder(view: View) : BaseAdapter.BaseViewHolder<CategoryPresentationModel>(view) {
        init {
            initSize()
            initItemViewClickListener()
        }

        override fun bind(model: CategoryPresentationModel, position: Int) {
            itemView.run {
                itemListCategoryImage.loadImg(model.image)
                itemListCategoryName.text = model.name
                itemListCategoryCountRecipe.text = model.countRecipes
            }
        }

        private fun initSize() {
            itemView.itemListCategoryCardView.run {
                val size = context.getScreenSize()
                layoutParams.width = (size.x * 0.47).toInt()
                layoutParams.height = layoutParams.width
            }
        }

        private fun initItemViewClickListener() {
            itemView.setOnClickListener {
                itemViewClickListener.invoke(dataList[adapterPosition])
            }
        }
    }
}