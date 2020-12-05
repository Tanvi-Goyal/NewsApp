package com.newsapp.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.newsapp.data.entities.News
import com.newsapp.data.local.NewsDao
import com.newsapp.data.paging.NewsPagingSource
import com.newsapp.data.remote.NewsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsDao: NewsDao,
    private val newsService: NewsService
) {

    fun getHeadlines(category: String): Flow<PagingData<News>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(newsService, category) }
        ).flow
    }
//    fun getHeadlines(category: String): LiveData<Resource<List<News>>> = liveData(Dispatchers.IO) {
//        emit(Resource.loading())
//
//        val local = { newsDao.getAllHeadlines(category) }.invoke().map { Resource.success(it) }
//        emitSource(local)
//
//        val response = suspend { getApiResponse { newsAPI.getHeadlines(category) } }.invoke()
//
//        if (response.status == Resource.Status.SUCCESS) {
//            response.data?.articles?.let { newsDao.insertAllHeadlines(it) }
//        }
//
//        response.message?.let {
//            emit(Resource.error(it))
//            emitSource(local)
//        }
//    }

    fun addToFavorites(id: Int, isFavorite: Int) = newsDao.addToFavorites(id, isFavorite)

    fun getFavoriteNews(category: String): LiveData<List<News>> =
        liveData(Dispatchers.IO) { emitSource(newsDao.getFavoriteNews(category)) }

    fun deleteDB() = newsDao.deleteAll()

    companion object {
        private const val PAGE_SIZE = 15
    }
}