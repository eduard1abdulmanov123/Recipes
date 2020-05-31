package abdulmanov.eduard.recipes.presentation.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<V> : RecyclerView.Adapter<BaseAdapter.BaseViewHolder<V>>() {

    protected val dataList = mutableListOf<V>()

    override fun onBindViewHolder(holder: BaseViewHolder<V>, position: Int) {
        holder.bind(dataList[position], position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateItems(itemsList: List<V>) {
        dataList.run {
            clear()
            addAll(itemsList)
        }
        notifyDataSetChanged()
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun bind(model: T, position: Int)
    }
}