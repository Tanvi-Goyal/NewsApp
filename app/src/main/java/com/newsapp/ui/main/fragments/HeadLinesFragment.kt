package com.newsapp.ui.main.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsapp.MainActivity
import com.newsapp.R
import com.newsapp.databinding.FragmentHeadLinesBinding
import com.newsapp.ui.main.adapters.NewAdapter
import com.newsapp.ui.main.adapters.NewsLoadStateAdapter
import com.newsapp.ui.main.adapters.TagsAdapter
import com.newsapp.ui.main.viewmodel.HeadlinesViewModel
import com.newsapp.utils.AppConstants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class HeadLinesFragment : Fragment() {

    private lateinit var binding: FragmentHeadLinesBinding
    private val viewModel: HeadlinesViewModel by viewModels()
    private lateinit var adapter: NewAdapter
    private lateinit var tagsAdapter: TagsAdapter

    private var currentCategory: String = ""
    private var job: Job? = null

    @Inject
    lateinit var preferences: SharedPreferences

    private fun getHeadlines(category: String) {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getHeadlines(category).collect {
                adapter.submitData(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHeadLinesBinding.inflate(inflater, container, false)
//        preferences.edit().putString("headline_category", "technology").apply()
//        preferences.edit().putInt("headline_position", 0).apply()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        preferences.getString("headline_category", "")?.let { viewModel.getHeadlines(it) }
        setUpTagsRecycler()
        setupRecyclerView()

        val category = savedInstanceState?.getString(AppConstants.LAST_SEARCH_CATEGORY)
            ?: AppConstants.DEFAULT_CATEGORY

        getHeadlines(category)
        initSearch(category)

//        setSwipeRefreshListeners()
//        setViewModelObservers()

        binding.btnFav.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.navigate_to_favorities)
        }

        binding.btnClearDB.setOnClickListener {
            viewModel.deleteDB()
            Toast.makeText(view.context, getString(R.string.error_no_news), Toast.LENGTH_SHORT)
                .show()
            setViewModelObservers()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(AppConstants.LAST_SEARCH_CATEGORY, currentCategory)
    }

    private fun initSearch(category: String) {

        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.recyclerViewHeadlines.scrollToPosition(0) }
        }
    }

//    private fun updateListBasedOnTag() {
//        binding.searchRepo.text.trim().let {
//            if (it.isNotEmpty()) {
//                binding.list.scrollToPosition(0)
//                search(it.toString())
//            }
//        }
//    }

//    private fun showEmptyList(show: Boolean) {
//        if (show) {
//            binding.emptyList.visibility = View.VISIBLE
//            binding.list.visibility = View.GONE
//        } else {
//            binding.emptyList.visibility = View.GONE
//            binding.list.visibility = View.VISIBLE
//        }
//    }

    private fun setUpTagsRecycler() {
        binding.recyclerTags.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        tagsAdapter = TagsAdapter()
        tagsAdapter.setData((activity as MainActivity).getTagList())
        binding.recyclerTags.adapter = tagsAdapter

        tagsAdapter.onItemClick = { category ->
//            viewModel.getHeadlines(category)
//            setViewModelObservers()
//            preferences.edit().putString("headline_category", category).apply()
//            binding.tvNoResult.visibility = View.GONE
////            adapter.clearData()

            binding.recyclerViewHeadlines.scrollToPosition(0)
            getHeadlines(category)

        }

    }

    private fun setupRecyclerView() {
        binding.recyclerViewHeadlines.layoutManager = LinearLayoutManager(context)
        adapter = NewAdapter()
        binding.recyclerViewHeadlines.adapter = adapter.withLoadStateHeaderAndFooter(
            header = NewsLoadStateAdapter { adapter.retry() },
            footer = NewsLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.recyclerViewHeadlines.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressbar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.tvNoResult.isVisible = loadState.source.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    requireContext(),
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

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

    }

    private fun setSwipeRefreshListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            preferences.getString("headline_category", "")?.let { viewModel.getHeadlines(it) }
            binding.tvNoResult.visibility = View.GONE
//            adapter.clearData()
//
            setViewModelObservers()
        }
    }

    private fun setViewModelObservers() {

        binding.swipeRefreshLayout.isRefreshing = false
        binding.progressbar.visibility = View.VISIBLE

//        viewModel.getNewsResult().observe(viewLifecycleOwner, Observer { newsList ->
//            if (!newsList.isNullOrEmpty()) {
//                binding.tvNoResult.visibility = View.GONE
//                binding.progressbar.visibility = View.GONE
//                binding.recyclerViewHeadlines.visibility = View.VISIBLE
//                adapter.setData(newsList as ArrayList<News>)
//            } else {
//                binding.recyclerViewHeadlines.visibility = View.GONE
//                binding.tvNoResult.visibility = View.GONE
//                binding.progressbar.visibility = View.GONE
//            }
//
//            binding.swipeRefreshLayout.isRefreshing = false
//        })
//
//        viewModel.getNewsError().observe(viewLifecycleOwner, Observer<String> {
//            Log.wtf("NEWS", it)
//            binding.swipeRefreshLayout.isRefreshing = false
//            binding.progressbar.visibility = View.GONE
//        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}