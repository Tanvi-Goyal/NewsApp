package com.newsapp.ui.main.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp.data.repositories.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteNewsViewModel @ViewModelInject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    fun getFavoriteNews(category: String) = repository.getFavoriteNews(category)

    fun addToFavorites(id: Int, isFavorite: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addToFavorites(id, isFavorite)
        }
    }
}
