package com.newsapp.ui.main.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.newsapp.data.entities.News
import com.newsapp.data.repositories.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HeadlinesViewModel @ViewModelInject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private var currentCategoryValue : String? = null
    private var currentHeadlinesResult : Flow<PagingData<News>>? = null

    fun getHeadlines(category: String) : Flow<PagingData<News>> {
        val lastResult = currentHeadlinesResult
        if(category == currentCategoryValue && lastResult != null) {
            return lastResult
        }

        currentCategoryValue = category
        val newResult = repository.getHeadlines(category).cachedIn(viewModelScope)
        currentHeadlinesResult = newResult
        return newResult
    }

    fun addToFavorites(id: Int, isFavorite: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addToFavorites(id, isFavorite)
        }
    }

    fun deleteDB() {
        viewModelScope.launch(Dispatchers.IO) {
//            repository.deleteDB()
        }
    }
}
