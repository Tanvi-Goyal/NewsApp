package com.newsapp.models

import com.newsapp.data.entities.News

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<News>?
)