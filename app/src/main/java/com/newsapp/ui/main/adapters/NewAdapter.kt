package com.newsapp.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.newsapp.R
import com.newsapp.data.entities.News
import com.newsapp.databinding.ItemHeadlineBinding
import com.newsapp.databinding.NewsLoadStateFooterViewBinding
import kotlinx.android.synthetic.main.item_headline.view.*

class NewAdapter : PagingDataAdapter<News, RecyclerView.ViewHolder>(NEWS_COMPARATOR) {

    var onItemClick: ((News) -> Unit)? = null
    var onFavClick: ((News, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHeadlineBinding.inflate(inflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val newsItem = getItem(position)
        if (newsItem != null) {
            (holder as NewsViewHolder).bind(newsItem)
        }
    }

    companion object {
        private val NEWS_COMPARATOR = object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItem: News, newItem: News): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: News, newItem: News): Boolean =
                oldItem == newItem
        }
    }

    inner class NewsViewHolder(val binding: ItemHeadlineBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var news: News

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(news)
            }
        }

        fun bind(news: News) {
            with(binding) {
                this.news = news
                binding.news = news
                binding.executePendingBindings()

                Glide.with(binding.root).load(news.urlToImage).into(binding.imageView)
                binding.iconFav.isChecked = news.isFavorite

                itemView.iconFav.setOnClickListener {
                    news.isFavorite = binding.iconFav.isChecked
                    onFavClick?.invoke(news, news.isFavorite)
                }
            }
        }
    }


}