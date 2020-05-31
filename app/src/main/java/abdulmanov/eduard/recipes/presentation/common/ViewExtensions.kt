package abdulmanov.eduard.recipes.presentation.common

import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun View.visibility(show: Boolean, type: Int = View.GONE) {

    if (show && visibility == View.VISIBLE) return

    if (!show && visibility == type) return

    visibility = if (show) View.VISIBLE else type
}

fun Context.getScreenSize(): Point {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return Point().apply {
        wm.defaultDisplay.getSize(this)
    }
}

fun ImageView.loadImg(imageUrl: String, placeholderRes: Int? = null) {
    Picasso.get().load(imageUrl).apply {
        placeholderRes?.let { placeholder(it) }
        fit().into(this@loadImg)
    }
}

fun ImageView.loadImg(imageUrl: Int, placeholderRes: Int? = null) {
    Picasso.get().load(imageUrl).apply {
        placeholderRes?.let { placeholder(it) }
        fit().centerCrop().into(this@loadImg)
    }
}