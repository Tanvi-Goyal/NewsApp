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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHeadLinesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getHeadlines()
        setupRecyclerView()
        setViewModelObservers()

        binding.btnFav.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.navigate_to_favorities)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewHeadlines.layoutManager = LinearLayoutManager(context)
        adapter = HeadlinesAdapter()

        adapter.onItemClick = { news ->
            val directions = news.url?.let { HeadLinesFragmentDirections.navigateToNewsDetail(it) }
            directions?.let { findNavController().navigate(it) }
        }

        binding.swipeRefreshLayout.setOnRefreshListener { setViewModelObservers() }
        binding.recyclerViewHeadlines.adapter = adapter
    }

    private fun setViewModelObservers() {

        binding.swipeRefreshLayout.isRefreshing = false
        binding.progressbar.visibility = View.VISIBLE

        viewModel.getNewsResult().observe(viewLifecycleOwner, Observer { newsList ->
            Log.wtf("NEWS", newsList[0].content)
            if (!newsList.isNullOrEmpty()) {
                adapter.setData(newsList as ArrayList<News>)
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