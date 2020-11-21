package com.newsapp.data.repositories

import com.newsapp.data.entities.News
import com.newsapp.data.local.NewsDao
import com.newsapp.data.remote.NewsAPI
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsDao: NewsDao,
    private val newsAPI: NewsAPI
) {

    fun getHeadlines(category: String): Observable<List<News>> {
        val observableFromDb = getHeadLinesFromDB(category)
        val observableFromApi = getHeadLinesFromAPI(category)
        return Observable.concatArrayEager(observableFromDb, observableFromApi)
    }

    private fun getHeadLinesFromAPI(category: String): Observable<List<News>> {
        return newsAPI.getHeadlines(category)
            .flatMap { response -> Observable.fromArray(response.articles!!) }
            .subscribeOn(Schedulers.io())
            .doOnNext { newsList ->
                // for adding category explicitly in the db
                val list = ArrayList<News>()
                for (news in newsList) {
                    val obj = News(
                        news.id,
                        news.source,
                        news.author,
                        news.title,
                        news.description,
                        news.url,
                        news.urlToImage,
                        news.publishedAt,
                        news.content,
                        news.isFavorite,
                        category
                    )
                    list.add(obj)
                }

                newsDao.insertAllHeadlines(list)
            }
    }

    private fun getHeadLinesFromDB(category: String): Observable<List<News>> {
        return newsDao.getAllHeadlines(category)
            .subscribeOn(Schedulers.io())
    }

    fun addToFavorites(id: Int, isFavorite: Int): Completable {
        return Completable.fromCallable { newsDao.addToFavorites(id, isFavorite) }
    }

    fun getFavoriteNews(category: String): Observable<List<News>> {
        return newsDao.getFavoriteNews(category).subscribeOn(Schedulers.io())
    }

    fun deleteDB(): Maybe<Unit> {
        return Maybe.fromAction<Unit> { newsDao.deleteAll() }.subscribeOn(Schedulers.io())
    }
}