package abdulmanov.eduard.recipes.presentation.ui.adapters

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.common.getScreenSize
import abdulmanov.eduard.recipes.presentation.common.inflate
import abdulmanov.eduard.recipes.presentation.common.loadImg
import abdulmanov.eduard.recipes.presentation.ui.model.CategoryViewModel
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_list_category.view.*

class CategoriesAdapter(
    private val itemViewClickListener:(CategoryViewModel) -> Unit
) : BaseAdapter<CategoryViewModel>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<CategoryViewModel> {
        return ViewHolder(parent.inflate(R.layout.item_list_category))
    }

    inner class ViewHolder(view: View):BaseAdapter.BaseViewHolder<CategoryViewModel>(view){

        init {
            initSize()
            initItemViewClickListener()
        }

        override fun bind(model: CategoryViewModel, position: Int) {
            itemView.run {
                item_list_category_image.loadImg(model.image)
                item_list_category_name.text = model.name
                item_list_category_count_recipe.text = model.countRecipes
            }
        }

        private fun initSize() {
            itemView.item_list_category_card_view.run {
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