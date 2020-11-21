package com.newsapp.models

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<News>?,
    val page: Int = 0
) {
    data class News(
        val id: Int = 0,
        val author: String?,
        val title: String,
        val description: String?,
        val url: String?,
        val urlToImage: String?,
        val publishedAt: String?,
        val content: String?
    )
}