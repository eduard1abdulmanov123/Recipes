package abdulmanov.eduard.recipes.presentation.ui.recipes.models

import android.os.Parcelable
import com.example.delegateadapter.delegate.diff.IComparableItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecipePresentationModel(
    val id: Long,
    val name: String,
    val image: String,
    val countIngredient: String,
    val countPortion: String,
    val time: String,
    val countLike: String,
    val countDislike: String
) : Parcelable, IComparableItem {
    override fun id() = id

    override fun content() = this
}