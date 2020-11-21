package com.newsapp.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.newsapp.data.entities.NewsModel
import com.newsapp.data.local.AppDatabase
import io.reactivex.Flowable
import javax.inject.Inject

class GetNewsRepository @Inject constructor(
    private val database: AppDatabase,
    private val remoteMediator: NewsRemoteMediator
) {

     fun getNews(category: String): Flowable<PagingData<NewsModel.News>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                maxSize = 30,
                prefetchDistance = 5,
                initialLoadSize = 40
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = { database.newsDao().getAllHeadlines(category) }
        ).flowable
    }

    fun getFavoriteNews(category: String): Flowable<PagingData<NewsModel.News>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                maxSize = 30,
                prefetchDistance = 5,
                initialLoadSize = 40
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = { database.newsDao().getFavoriteNews(category) }
        ).flowable
    }
}