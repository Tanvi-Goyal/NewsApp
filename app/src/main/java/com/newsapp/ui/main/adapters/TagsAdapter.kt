package com.newsapp.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newsapp.R
import com.newsapp.databinding.ItemTagBinding

class TagsAdapter : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null
    var adapterList: ArrayList<String> = ArrayList()
    var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTagBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = adapterList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(adapterList[position])

        if (position == selectedPosition) {
            holder.binding.constraint.setBackgroundColor(holder.itemView.resources.getColor(R.color.red))
        }
    }

    fun setData(data: ArrayList<String>) {
        adapterList = data
        notifyDataSetChanged()
    }

    fun clearData() {
        adapterList.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            with(binding) {
                binding.tag = item
                binding.executePendingBindings()

                binding.constraint.setBackgroundColor(itemView.resources.getColor(R.color.black))
            }
        }

        init {
            itemView.setOnClickListener {
                val previousPosition = selectedPosition
                notifyItemChanged(previousPosition)
                selectedPosition = adapterPosition
                notifyItemChanged(selectedPosition)

                onItemClick?.invoke(adapterList[adapterPosition])

            }
        }
    }
}
