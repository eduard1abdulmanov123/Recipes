package abdulmanov.eduard.recipes.presentation.ui.base

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

open class LinearInfiniteScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val visibleThreshold: Int,
    private val funcEnd: () -> Unit
) : RecyclerView.OnScrollListener() {

    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    var state: ScrollState = ScrollState.Normal

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0) {
            visibleItemCount = recyclerView.childCount
            totalItemCount = layoutManager.itemCount
            firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

            if (state == ScrollState.Normal && state != ScrollState.Full && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                state = ScrollState.Loading
                recyclerView.post(funcEnd)
            }
        }
    }

    sealed class ScrollState {
        object Normal : ScrollState()
        object Loading : ScrollState()
        object Full : ScrollState()
    }

}