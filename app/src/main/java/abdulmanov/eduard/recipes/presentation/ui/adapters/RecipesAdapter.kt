package abdulmanov.eduard.recipes.presentation.ui.adapters

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.common.inflate
import abdulmanov.eduard.recipes.presentation.common.loadImg
import abdulmanov.eduard.recipes.presentation.common.visibilityGone
import abdulmanov.eduard.recipes.presentation.ui.model.LoadingViewModel
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeViewModel
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_list_loading.view.*
import kotlinx.android.synthetic.main.item_list_recipe.view.*

/*
class RecipesAdapter(
    private val itemViewClickListener:(RecipeViewModel)->Unit,
    private val repeatClickListener: () -> Unit
):BaseAdapter<ItemView>() {

    companion object{
        private const val RECIPE_TYPE = 0
        private const val LOADING_TYPE = 1
    }

    private val loadingItem:LoadingViewModel? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ItemView> {
        return if(viewType == RECIPE_TYPE)
            RecipeViewHolder(parent.inflate(R.layout.item_list_recipe))
        else
            LoadingViewHolder(parent.inflate(R.layout.item_list_loading))
    }



    override fun getItemCount(): Int {
        return dataList.size + if(loadingItem != null) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if(loadingItem != null && position == itemCount -1 )
            LOADING_TYPE
        else
            RECIPE_TYPE
    }

    inner class RecipeViewHolder(view: View):BaseAdapter.BaseViewHolder<RecipeViewModel>(view){

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

    inner class LoadingViewHolder(view: View):BaseAdapter.BaseViewHolder<LoadingViewModel>(view){

        init {
            itemView.item_list_loading_repeat.setOnClickListener {
                repeatClickListener.invoke()
            }
        }

        override fun bind(model: LoadingViewModel, position: Int) {
            itemView.run {
                item_list_loading_progress_bar.visibilityGone(model == LoadingViewModel.LoadingViewModelState.Loading)
                item_list_loading_repeat.visibilityGone(model == LoadingViewModel.Refresh)
            }
        }

    }

}*/
