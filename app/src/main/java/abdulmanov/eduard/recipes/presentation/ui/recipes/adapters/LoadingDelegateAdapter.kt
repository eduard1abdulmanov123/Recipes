package abdulmanov.eduard.recipes.presentation.ui.recipes.adapters

import abdulmanov.eduard.recipes.R
import abdulmanov.eduard.recipes.presentation.common.visibility
import abdulmanov.eduard.recipes.presentation.ui.recipes.models.LoadingPresentationModel
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
            itemListLoadingRepeat.setOnClickListener {
                refreshClickListener.invoke()
            }
            itemListLoadingProgressBar.visibility(item.state == LoadingPresentationModel.LoadingViewModelState.Loading)
            itemListLoadingRepeat.visibility(item.state == LoadingPresentationModel.LoadingViewModelState.Error)
        }
    }
}