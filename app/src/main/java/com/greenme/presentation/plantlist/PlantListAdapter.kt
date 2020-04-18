package com.greenme.presentation.plantlist

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.greenme.R
import com.greenme.domain.model.Plant
import com.greenme.util.inflate
import kotlinx.android.synthetic.main.item_list_plant.view.*


class PlantListAdapter constructor(private val itemClick: (Plant) -> Unit) :
    ListAdapter<Plant, PlantListAdapter.ViewHolder>(PlantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class ViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(parent.inflate(R.layout.item_list_plant)) {

        fun bind(item: Plant) {
            itemView.textViewName.text = item.name
            itemView.setOnClickListener { itemClick.invoke(item) }
        }
    }
}
private class PlantDiffCallback : DiffUtil.ItemCallback<Plant>() {
    override fun areItemsTheSame(oldItem: Plant, newItem: Plant): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Plant, newItem: Plant): Boolean =
        oldItem == newItem
}