package com.newsapp.data.repositories

import android.util.Log
import com.newsapp.data.entities.News
import com.newsapp.data.local.NewsDao
import com.newsapp.data.remote.NewsAPI
import io.reactivex.Observable
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsDao: NewsDao,
    private val newsAPI: NewsAPI
) {

    fun getHeadlines(category: String): Observable<List<News>> {
        val observableFromApi = getHeadLinesFromAPI(category)
        val observableFromDb = getHeadLinesFromDB()
        return Observable.concatArrayEager(observableFromApi, observableFromDb)
    }

    private fun getHeadLinesFromAPI(category: String): Observable<List<News>> {
        return newsAPI.getHeadlines(category)
            .flatMap { response -> Observable.fromArray(response.articles!!) }
            .doOnNext { newsList ->
                newsDao.insertAllHeadlines(newsList)
            }
    }

    private fun getHeadLinesFromDB(): Observable<List<News>> {
        return newsDao.getAllHeadlines()
            .doOnNext {
                Log.e("REPOSITORY DB *** ", it.size.toString())
            }
    }
}