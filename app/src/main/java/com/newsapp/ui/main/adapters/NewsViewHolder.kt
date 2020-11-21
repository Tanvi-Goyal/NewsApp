package com.newsapp.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newsapp.R
import com.newsapp.data.entities.NewsModel
import com.newsapp.databinding.ItemHeadlineBinding

class NewsViewHolder(private val binding: ItemHeadlineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: NewsModel.News) {
        with(movie) {
        }
    }

    companion object {
        fun create(parent: ViewGroup): NewsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_headline, parent, false)

            val binding = ItemHeadlineBinding.bind(view)

            return NewsViewHolder(
                binding
            )
        }
    }
}