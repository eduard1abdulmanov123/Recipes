package abdulmanov.eduard.recipes.presentation.ui.adapters

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.ui.base.HorizontalItemDecoration
import abdulmanov.eduard.recipes.presentation.ui.model.BestRecipesViewModel
import abdulmanov.eduard.recipes.presentation.ui.model.RecipeViewModel
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.delegateadapter.delegate.KDelegateAdapter
import kotlinx.android.synthetic.main.item_list_container_best_recipes.*
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator

class BestRecipesDelegateAdapter(
    private val itemViewClickListener:(RecipeViewModel)->Unit
):KDelegateAdapter<BestRecipesViewModel>() {

    override fun getLayoutId() = R.layout.item_list_container_best_recipes

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is BestRecipesViewModel
    }

    override fun onCreated(view: View) {
        super.onCreated(view)

        val indicator = view.findViewById<ScrollingPagerIndicator>(
            R.id.item_list_container_best_recipes_recycler_view_indicator
        )

        view.findViewById<RecyclerView>(R.id.item_list_container_best_recipes_recycler_view).run{
            setHasFixedSize(true)
            addItemDecoration(HorizontalItemDecoration(6,6,context))
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter = BestRecipesAdapter(itemViewClickListener)
            indicator.attachToRecyclerView(this)
        }
    }

    override fun onBind(item: BestRecipesViewModel, viewHolder: KViewHolder) {
        viewHolder.run {
            (item_list_container_best_recipes_recycler_view.adapter as BestRecipesAdapter).updateItems(item.recipes)
        }
    }

}