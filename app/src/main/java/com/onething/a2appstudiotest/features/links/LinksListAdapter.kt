package com.onething.a2appstudiotest.features.links

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onething.a2appstudiotest.databinding.ListItemListBinding
import com.onething.a2appstudiotest.model.Links

class LinksListAdapter : ListAdapter<Links, LinksListAdapter.LinksViewHolder>(LinksDiffUtil()) {

    private var isSelectMode = false
    private val selectedItem = mutableListOf<Links>()

    class LinksViewHolder(private val binding: ListItemListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Links) {
            binding.data = data
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinksViewHolder {
        return LinksViewHolder(
            ListItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: LinksViewHolder, position: Int) {
        val links = getItem(position)

        addRecyclerViewLongClick(holder, links)

        addRecyclerViewOnClick(holder, links)

        if (isSelectMode) {
            holder.itemView.setBackgroundColor(Color.LTGRAY)
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }

        holder.bind(links)

    }

    fun getSelectedItem() : MutableList<Links> {
        return selectedItem
    }

    fun resetSelectState() {
        isSelectMode = false
        selectedItem.clear()
        notifyDataSetChanged()
    }

    private fun addRecyclerViewLongClick(holder: LinksViewHolder, links: Links) {
        holder.itemView.setOnLongClickListener {
            isSelectMode = true
            if (selectedItem.contains(links)) {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT)
                selectedItem.remove(links)
            } else {
                holder.itemView.setBackgroundColor(Color.LTGRAY)
                selectedItem.add(links)
            }

            if (selectedItem.isEmpty()) isSelectMode = false
            return@setOnLongClickListener true
        }
    }

    private fun addRecyclerViewOnClick(holder: LinksViewHolder, links: Links) {
        holder.itemView.setOnClickListener {
            if (isSelectMode) {
                if (selectedItem.contains(links)) {
                    holder.itemView.setBackgroundColor(Color.TRANSPARENT)
                    selectedItem.remove(links)
                } else {
                    holder.itemView.setBackgroundColor(Color.LTGRAY)
                    selectedItem.add(links)
                }
            }
            if (selectedItem.isEmpty()) isSelectMode = false
        }
    }


}

class LinksDiffUtil : DiffUtil.ItemCallback<Links>() {
    override fun areItemsTheSame(oldItem: Links, newItem: Links) =
        oldItem.link == newItem.link

    override fun areContentsTheSame(oldItem: Links, newItem: Links) =
        oldItem == newItem
}