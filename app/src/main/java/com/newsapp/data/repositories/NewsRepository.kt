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

    fun getHeadlines(): Observable<List<News>> {
        val observableFromApi = getHeadLinesFromAPI()
        return observableFromApi
//        val observableFromDb = getHeadLinesFromDB()
//        return Observable.concatArrayEager(observableFromApi, observableFromDb)
    }

    private fun getHeadLinesFromAPI(): Observable<List<News>> {
        return newsAPI.getHeadlines()
            .flatMap { response -> Observable.fromArray(response.articles!!) }
            .doOnNext { newsList ->
                newsDao.insertAllHeadlines(newsList)
            }
    }

//    private fun getHeadLinesFromDB(): Observable<List<News>> {
//        return newsDao.getAllHeadlines()
//            .toObservable()
//            .doOnNext {
//                Log.e("REPOSITORY DB *** ", it.size.toString())
//            }
//    }
}