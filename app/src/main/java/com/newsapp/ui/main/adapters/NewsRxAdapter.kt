package com.newsapp.ui.main.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.newsapp.data.entities.NewsModel

class NewsRxAdapter : PagingDataAdapter<NewsModel.News, NewsViewHolder>(
    COMPARATOR
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsViewHolder {
        return NewsViewHolder.create(
            parent
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<NewsModel.News>() {
            override fun areItemsTheSame(
                oldItem: NewsModel.News,
                newItem: NewsModel.News
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: NewsModel.News,
                newItem: NewsModel.News
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}