package com.newsapp.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.newsapp.data.entities.News
import com.newsapp.databinding.ItemHeadlineBinding
import kotlinx.android.synthetic.main.item_headline.view.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    var onItemClick: ((News) -> Unit)? = null
    var onFavClick: ((News, Boolean) -> Unit)? = null
    var adapterList: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHeadlineBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = adapterList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNews = adapterList[position]
        holder.bind(currentNews)
    }

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
        fun bind(news: News) {
            with(binding) {
                binding.news = news
                binding.executePendingBindings()

                Glide.with(binding.root).load(news.urlToImage).into(binding.imageView)
                binding.iconFav.isChecked = news.isFavorite

                itemView.iconFav.setOnClickListener {
                    news.isFavorite = binding.iconFav.isChecked
                    onFavClick?.invoke(adapterList[adapterPosition], news.isFavorite)
                }
            }
        }

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterList[adapterPosition])
            }
        }
    }
}
