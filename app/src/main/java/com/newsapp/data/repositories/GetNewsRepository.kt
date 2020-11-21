package com.newsapp.data.repositories

import androidx.paging.PagingData
import com.newsapp.data.entities.NewsModel
import io.reactivex.Flowable

interface GetNewsRepository {
    fun getNews(category : String): Flowable<PagingData<NewsModel.News>>
}