package abdulmanov.eduard.recipes.presentation.ui.base

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

open class LinearInfiniteScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val visibleThreshold: Int,
    private val funcEnd: () -> Unit
) : RecyclerView.OnScrollListener() {

    private var previousTotal = 0
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var state = StatePagination.LOADING

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                visibleItemCount = recyclerView.childCount
                totalItemCount = layoutManager.itemCount
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                Log.d("ScrollListener","$visibleItemCount $totalItemCount $firstVisibleItem")
                if (state == StatePagination.LOADING && totalItemCount > previousTotal + 1) { // +1 так как вставляется loading
                    state = StatePagination.NOT_LOADING
                    previousTotal = totalItemCount
                    Log.d("ScrollListener",state.toString())
                }

                if (state == StatePagination.NOT_LOADING && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    state = StatePagination.LOADING
                    recyclerView.post(funcEnd)
                    Log.d("ScrollListener",state.toString())
                }
            } else if (dy < 0) {

            }
    }

    fun setStartState(){
        previousTotal = 0
        firstVisibleItem = 0
        visibleItemCount = 0
        totalItemCount = 0
        state = StatePagination.LOADING
    }

    enum class StatePagination {
       LOADING,NOT_LOADING
    }
}