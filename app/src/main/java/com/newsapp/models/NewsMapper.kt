package com.newsapp.models

import com.newsapp.data.entities.NewsModel

class NewsMapper {
    fun transform(response: NewsResponse): NewsModel {
        return with(response) {
            NewsModel(
                total = totalResults,
                page = page,
                news = articles!!.map { currentNews ->
                    NewsModel.News(
                        currentNews.id,
                    currentNews.author,
                    currentNews.title,
                    currentNews.description,
                    currentNews.url,
                    currentNews.urlToImage,
                    currentNews.publishedAt,
                    currentNews.content,
                    currentNews.isFavorite: Boolean = false,
                    currentNews.category
                    )
                }
            )
        }
    }
}