package abdulmanov.eduard.recipes.presentation.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ViewGroup.inflate(layoutId:Int, attachToRoot:Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun View.visibilityGone(show:Boolean){

    if(show && visibility==View.VISIBLE) return

    if(!show && visibility==View.GONE) return

    visibility = if(show) View.VISIBLE else View.GONE
}

fun View.visibilityInvisible(show:Boolean){
    visibility = if(show) View.VISIBLE else View.INVISIBLE
}

fun ImageView.loadImg(imageUrl: String, placeholderRes: Int? = null) {
    Picasso.get().load(imageUrl).apply {
        placeholderRes?.let {placeholder(it) }
        fit().into(this@loadImg)
    }
}