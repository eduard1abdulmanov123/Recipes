package abdulmanov.eduard.recipes.presentation.ui.adapters

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.ui.base.HorizontalItemDecoration
import abdulmanov.eduard.recipes.presentation.ui.model.CategoryViewModel
import abdulmanov.eduard.recipes.presentation.ui.model.CategoryWithRecipesViewModel
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeViewModel
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.delegateadapter.delegate.BaseViewHolder
import com.example.delegateadapter.delegate.KDelegateAdapter
import kotlinx.android.synthetic.main.item_list_container_category_recipes.*
import kotlinx.android.synthetic.main.item_list_container_category_recipes.view.*

class CategoryWithRecipesDelegateAdapter(
    private val moreClickListener:(CategoryViewModel) -> Unit,
    private val itemViewClickListener:(RecipeViewModel) -> Unit
):KDelegateAdapter<CategoryWithRecipesViewModel>() {

    override fun getLayoutId() = R.layout.item_list_container_category_recipes

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is CategoryWithRecipesViewModel
    }

    override fun onCreated(view: View) {
        super.onCreated(view)

        view.findViewById<RecyclerView>(R.id.item_list_container_category_recipes_recycler_view).run{
            setHasFixedSize(true)
            addItemDecoration(HorizontalItemDecoration(6,6,context))
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = RecipesAdapter(itemViewClickListener)
        }
    }

    override fun onBind(item: CategoryWithRecipesViewModel, viewHolder: KViewHolder) {
        viewHolder.run {
            item_list_container_category_recipes_more.setOnClickListener {
                moreClickListener.invoke(item.category)
            }
            item_list_container_category_recipes.text = item.category.name
            (item_list_container_category_recipes_recycler_view.adapter as RecipesAdapter).updateItems(item.recipes)
        }
    }

}