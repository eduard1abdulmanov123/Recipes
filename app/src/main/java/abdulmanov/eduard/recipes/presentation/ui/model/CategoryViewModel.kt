package abdulmanov.eduard.recipes.presentation.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryViewModel(
    val name:String,
    val value:String
):Parcelable