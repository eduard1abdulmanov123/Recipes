package abdulmanov.eduard.recipes.presentation.ui.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryVM(
    @DrawableRes
    val image: Int,
    val name: String,
    val value: String,
    val countRecipes: String
) : Parcelable