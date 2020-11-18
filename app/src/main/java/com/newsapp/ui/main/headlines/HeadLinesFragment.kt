package com.newsapp.ui.main.headlines

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsapp.R
import com.newsapp.data.entities.News
import com.newsapp.databinding.FragmentHeadLinesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeadLinesFragment : Fragment() {

    private lateinit var binding: FragmentHeadLinesBinding
    private val viewModel: HeadlinesViewModel by viewModels()
    private lateinit var adapter: HeadlinesAdapter
    private lateinit var tagsAdapter: TagsAdapter
    private val tagList = arrayListOf<String>(
        "technology", "sports", "science", "health", "general", "entertainment", "business"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHeadLinesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getHeadlines("technology")
        setUpTagsRecycler()
        setupRecyclerView()
        setViewModelObservers()

        binding.btnFav.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.navigate_to_favorities)
        }
    }

    private fun setUpTagsRecycler() {
        binding.recyclerTags.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        tagsAdapter = TagsAdapter()
        tagsAdapter.setData(tagList)

        tagsAdapter.onItemClick = { category ->
            adapter.clearData()
            viewModel.getHeadlines(category)
            setViewModelObservers()
        }

        binding.recyclerTags.adapter = tagsAdapter
    }

    private fun setupRecyclerView() {
        binding.recyclerViewHeadlines.layoutManager = LinearLayoutManager(context)
        adapter = HeadlinesAdapter()

        adapter.onItemClick = { news ->
            val directions = news.url?.let { HeadLinesFragmentDirections.navigateToNewsDetail(it) }
            directions?.let { findNavController().navigate(it) }
        }

        adapter.onFavClick = { news, isFav ->
            if (isFav) {
                viewModel.addToFavorites(news.id!!, 1)
            } else viewModel.addToFavorites(news.id!!, 0)
        }

        binding.swipeRefreshLayout.setOnRefreshListener { setViewModelObservers() }
        binding.recyclerViewHeadlines.adapter = adapter
    }

    private fun setViewModelObservers() {

        binding.swipeRefreshLayout.isRefreshing = false
        binding.progressbar.visibility = View.VISIBLE
        binding.tvNoResult.visibility = View.GONE

        viewModel.getNewsResult().observe(viewLifecycleOwner, Observer { newsList ->
            if (!newsList.isNullOrEmpty()) {
                binding.tvNoResult.visibility = View.GONE
                binding.progressbar.visibility = View.GONE
                adapter.setData(newsList as ArrayList<News>)
            } else {
                binding.tvNoResult.visibility = View.VISIBLE
                binding.progressbar.visibility = View.GONE
            }

            binding.swipeRefreshLayout.isRefreshing = false
        })

        viewModel.getNewsError().observe(viewLifecycleOwner, Observer<String> {
            Log.wtf("NEWS", it)
            binding.swipeRefreshLayout.isRefreshing = false
            binding.progressbar.visibility = View.GONE
        })
    }
}