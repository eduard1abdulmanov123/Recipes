package abdulmanov.eduard.recipes.presentation.ui.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryPresentationModel(
    @DrawableRes
    val image: Int,
    val name: String,
    val value: String,
    val countRecipes: String
) : Parcelable