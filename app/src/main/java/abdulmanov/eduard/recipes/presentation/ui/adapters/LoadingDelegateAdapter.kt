package abdulmanov.eduard.recipes.presentation.ui.adapters

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.common.visibility
import abdulmanov.eduard.recipes.presentation.ui.models.LoadingPresentationModel
import com.example.delegateadapter.delegate.KDelegateAdapter
import kotlinx.android.synthetic.main.item_list_loading.*

class LoadingDelegateAdapter (
    private val refreshClickListener:()->Unit?
) : KDelegateAdapter<LoadingPresentationModel>(){

    override fun getLayoutId() = R.layout.item_list_loading

    override fun isForViewType(items: MutableList<*>, position: Int): Boolean {
        return items[position] is LoadingPresentationModel
    }

    override fun onBind(item: LoadingPresentationModel, viewHolder: KViewHolder) {
        viewHolder.run {
            item_list_loading_repeat.setOnClickListener {
               refreshClickListener.invoke()
            }
            item_list_loading_progress_bar.visibility(item.state == LoadingPresentationModel.LoadingViewModelState.Loading)
            item_list_loading_repeat.visibility(item.state == LoadingPresentationModel.LoadingViewModelState.Error)
        }
    }
}