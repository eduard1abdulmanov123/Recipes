package abdulmanov.eduard.recipes.presentation.ui.base

import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalItemDecoration(
    private var offsetLeftRight: Int,
    private var offsetTopBottom: Int,
    context: Context
) : RecyclerView.ItemDecoration() {

    init {
        offsetLeftRight *= (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        offsetTopBottom *= (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        when (parent.getChildAdapterPosition(view)) {
            0 -> outRect.right = offsetLeftRight
            parent.adapter!!.itemCount - 1 -> outRect.left = offsetLeftRight
            else -> {
                outRect.right = offsetLeftRight
                outRect.left = offsetLeftRight
            }
        }

        outRect.top = offsetTopBottom
        outRect.bottom = offsetTopBottom
    }
}