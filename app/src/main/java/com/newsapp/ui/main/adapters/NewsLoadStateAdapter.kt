package com.newsapp.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.newsapp.databinding.NewsLoadStateFooterViewBinding

class NewsLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<NewsLoadStateAdapter.NewsLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: NewsLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ): NewsLoadStateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NewsLoadStateFooterViewBinding.inflate(inflater, parent, false)
        return NewsLoadStateViewHolder(binding, retry)
    }

    inner class NewsLoadStateViewHolder(
        private val binding: NewsLoadStateFooterViewBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState !is LoadState.Loading
            binding.errorMsg.isVisible = loadState !is LoadState.Loading
        }
    }
}