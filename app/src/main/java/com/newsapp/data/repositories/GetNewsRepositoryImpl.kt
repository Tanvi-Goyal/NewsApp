package com.newsapp.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.newsapp.data.entities.NewsModel
import com.newsapp.data.local.AppDatabase
import io.reactivex.Flowable
import javax.inject.Inject

class GetNewsRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val remoteMediator: NewsRemoteMediator
) : GetNewsRepository {

    override fun getNews(category: String): Flowable<PagingData<NewsModel.News>> {
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
}