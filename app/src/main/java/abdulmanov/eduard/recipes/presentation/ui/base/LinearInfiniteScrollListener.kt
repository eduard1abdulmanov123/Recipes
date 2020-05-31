package abdulmanov.eduard.recipes.presentation.ui.base

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
    var state: PaginationState = PaginationState.Allow

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0) {
            visibleItemCount = recyclerView.childCount
            totalItemCount = layoutManager.itemCount
            firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

            if (state == PaginationState.Allow && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                recyclerView.post(funcEnd)
            }
        }
    }

    sealed class PaginationState {
        object Allow : PaginationState()
        object Ban : PaginationState()
    }
}