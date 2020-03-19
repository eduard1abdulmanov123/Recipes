package abdulmanov.eduard.recipes.presentation.ui.adapters

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.common.visibilityGone
import abdulmanov.eduard.recipes.presentation.ui.model.LoadingViewModel
import android.util.Log
import com.example.delegateadapter.delegate.KDelegateAdapter
import kotlinx.android.synthetic.main.item_list_loading.*

class LoadingDelegateAdapter (
    private val refreshClickListener:()->Unit
) : KDelegateAdapter<LoadingViewModel>(){

    override fun getLayoutId() = R.layout.item_list_loading

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is LoadingViewModel
    }

    override fun onBind(item: LoadingViewModel, viewHolder: KViewHolder) {
        viewHolder.run {
            item_list_loading_repeat.setOnClickListener {
               refreshClickListener.invoke()
            }
            item_list_loading_progress_bar.visibilityGone(item.state == LoadingViewModel.LoadingViewModelState.Loading)
            item_list_loading_repeat.visibilityGone(item.state == LoadingViewModel.LoadingViewModelState.Error)
        }
    }
}