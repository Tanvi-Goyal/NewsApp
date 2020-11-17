package com.newsapp.ui.main.headlines

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.newsapp.data.entities.News
import com.newsapp.databinding.ItemHeadlineBinding

class HeadlinesAdapter : RecyclerView.Adapter<HeadlinesAdapter.ViewHolder>() {

    var onItemClick: ((News) -> Unit)? = null
    var adapterList: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHeadlineBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = adapterList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(adapterList[position])

    fun setData(data: ArrayList<News>) {
        adapterList = data
        notifyDataSetChanged()
    }

    fun clearData() {
        adapterList.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemHeadlineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: News) {
            with(binding) {
                binding.news = item
                binding.executePendingBindings()

                Glide.with(binding.root)
                    .load(item.urlToImage)
                    .into(binding.imageView)
            }
        }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterList[adapterPosition])

            }
        }
    }
}
