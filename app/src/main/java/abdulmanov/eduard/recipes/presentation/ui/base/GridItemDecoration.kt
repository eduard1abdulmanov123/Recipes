package abdulmanov.eduard.recipes.presentation.ui.base

import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridItemDecoration(
    private var offsetLeftRight:Int,
    private var offsetTopBottom:Int,
    context: Context
): RecyclerView.ItemDecoration() {

    init {
        offsetLeftRight *= (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        offsetTopBottom *= (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        when (parent.getChildAdapterPosition(view)) {
            0 -> {
                outRect.right = offsetLeftRight
                outRect.bottom = offsetTopBottom
            }
            1 -> {
                outRect.left = offsetLeftRight
                outRect.bottom = offsetTopBottom
            }
            parent.adapter!!.itemCount - 2 -> {
                if (((parent.adapter!!.itemCount - 2) % 2) != 0) {
                    outRect.left = offsetLeftRight
                    outRect.top = offsetTopBottom
                    outRect.bottom = offsetTopBottom
                } else {
                    outRect.right = offsetLeftRight
                    outRect.top = offsetTopBottom
                }
            }
            parent.adapter!!.itemCount - 1 -> {
                if ((parent.adapter!!.itemCount - 1) % 2 != 0) {
                    outRect.left = offsetLeftRight
                    outRect.top = offsetTopBottom
                } else {
                    outRect.top = offsetTopBottom
                }
            }
            else -> {
                if ((parent.getChildAdapterPosition(view) + 1) % 2 == 0) {
                    outRect.left = offsetLeftRight
                    outRect.top = offsetTopBottom
                    outRect.bottom = offsetTopBottom
                } else {
                    outRect.right = offsetLeftRight
                    outRect.top = offsetTopBottom
                    outRect.bottom = offsetTopBottom
                }
            }
        }
    }
}