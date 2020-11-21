package com.newsapp.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsapp.MainActivity
import com.newsapp.R
import com.newsapp.data.entities.News
import com.newsapp.databinding.FragmentHeadLinesBinding
import com.newsapp.ui.main.adapters.NewsAdapter
import com.newsapp.ui.main.adapters.TagsAdapter
import com.newsapp.ui.main.viewmodel.HeadlinesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeadLinesFragment : Fragment() {

    private lateinit var binding: FragmentHeadLinesBinding
    private val viewModel: HeadlinesViewModel by viewModels()
    private lateinit var adapter: NewsAdapter
    private lateinit var tagsAdapter: TagsAdapter
    private var selectedCategory : String = ""

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
        setSwipeRefreshListeners()
        setViewModelObservers()

        binding.btnFav.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.navigate_to_favorities)
        }

        binding.btnClearDB.setOnClickListener {
            viewModel.deleteDB()
            Toast.makeText(view.context, getString(R.string.error_no_news), Toast.LENGTH_SHORT).show()
            setViewModelObservers()
        }
    }

    private fun setUpTagsRecycler() {
        binding.recyclerTags.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        tagsAdapter = TagsAdapter()
        tagsAdapter.setData((activity as MainActivity).getTagList())

        tagsAdapter.onItemClick = { category ->
            selectedCategory = category
            viewModel.getHeadlines(category)
            binding.tvNoResult.visibility = View.GONE
            adapter.clearData()

            setViewModelObservers()
        }

        binding.recyclerTags.adapter = tagsAdapter
    }

    private fun setupRecyclerView() {
        binding.recyclerViewHeadlines.layoutManager = LinearLayoutManager(context)
        adapter = NewsAdapter()

        adapter.onItemClick = { news ->
            val directions = news.url?.let {
                HeadLinesFragmentDirections.navigateToNewsDetail(
                    it
                )
            }
            directions?.let { findNavController().navigate(it) }
        }

        adapter.onFavClick = { news, isFav ->
            if (isFav) {
                viewModel.addToFavorites(news.id!!, 1)
            } else viewModel.addToFavorites(news.id!!, 0)
        }

        binding.recyclerViewHeadlines.adapter = adapter
    }

    private fun setSwipeRefreshListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getHeadlines(selectedCategory)
            binding.tvNoResult.visibility = View.GONE
            adapter.clearData()

            setViewModelObservers()
        }
    }

    private fun setViewModelObservers() {

        binding.swipeRefreshLayout.isRefreshing = false
        binding.progressbar.visibility = View.VISIBLE

        viewModel.getNewsResult().observe(viewLifecycleOwner, Observer { newsList ->
            if (!newsList.isNullOrEmpty()) {
                binding.tvNoResult.visibility = View.GONE
                binding.progressbar.visibility = View.GONE
                binding.recyclerViewHeadlines.visibility = View.VISIBLE
                adapter.setData(newsList as ArrayList<News>)
            } else {
                binding.recyclerViewHeadlines.visibility = View.GONE
                binding.tvNoResult.visibility = View.GONE
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

    override fun onDestroyView() {
        viewModel.disposeElements()
        super.onDestroyView()
    }
}