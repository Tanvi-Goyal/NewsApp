package com.newsapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsapp.MainActivity
import com.newsapp.data.entities.News
import com.newsapp.databinding.FragmentFavoritiesBinding
import com.newsapp.ui.main.adapters.NewsAdapter
import com.newsapp.ui.main.adapters.TagsAdapter
import com.newsapp.ui.main.viewmodel.FavoriteNewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritiesBinding
    private val viewModel: FavoriteNewsViewModel by viewModels()
    private lateinit var adapter: NewsAdapter
    private lateinit var tagsAdapter: TagsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoriteNews("technology")
        setUpTagsRecycler()
        setupRecyclerView()
        setViewModelObservers()
    }

    private fun setUpTagsRecycler() {
        binding.recyclerTags.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        tagsAdapter = TagsAdapter()
        tagsAdapter.setData((activity as MainActivity).getTagList())

        tagsAdapter.onItemClick = { category ->
            binding.tvNoResult.visibility = View.GONE
            adapter.clearData()
            viewModel.getFavoriteNews(category)
            setViewModelObservers()
        }

        binding.recyclerTags.adapter = tagsAdapter
    }

    private fun setupRecyclerView() {
        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(context)
        adapter = NewsAdapter()

        adapter.onItemClick = { news ->
            val directions = news.url?.let { FavoritesFragmentDirections.navigateToNewsDetail(it) }
            directions?.let { findNavController().navigate(it) }
        }

        adapter.onFavClick = { news, isFav ->
            if (isFav) {
                viewModel.addToFavorites(news.id!!, 1)
            } else viewModel.addToFavorites(news.id!!, 0)
        }

        binding.recyclerViewFavorite.adapter = adapter
    }

    private fun setViewModelObservers() {

        binding.progressbar.visibility = View.VISIBLE

        viewModel.getNewsResult().observe(viewLifecycleOwner, Observer { newsList ->
            if (!newsList.isNullOrEmpty()) {
                binding.tvNoResult.visibility = View.GONE
                binding.progressbar.visibility = View.GONE
                adapter.setData(newsList as ArrayList<News>)
            } else {
                binding.tvNoResult.visibility = View.GONE
                binding.progressbar.visibility = View.GONE
            }

        })

        viewModel.getNewsError().observe(viewLifecycleOwner, Observer<String> {
            Log.wtf("NEWS", it)
            binding.progressbar.visibility = View.GONE
        })
    }

    override fun onDestroyView() {
        viewModel.disposeElements()
        super.onDestroyView()
    }
}