package com.newsapp.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxRemoteMediator
import com.newsapp.data.entities.NewsModel
import com.newsapp.data.local.AppDatabase
import com.newsapp.data.remote.NewsAPI
import com.newsapp.models.NewsMapper
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val newsAPI: NewsAPI,
    private val db: AppDatabase,
    private val mapper: NewsMapper
) : RxRemoteMediator<Int, NewsModel.News>() {

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, NewsModel.News>
    ): Single<MediatorResult> {

        return Single.just(loadType)
            .subscribeOn(Schedulers.io())
            .map {
                when (it) {
                    LoadType.REFRESH -> {
                        val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)

                        remoteKeys?.nextKey?.minus(1) ?: 1
                    }
                    LoadType.PREPEND -> {
                        val remoteKeys = getRemoteKeyForFirstItem(state)
                            ?: throw InvalidObjectException("Result is empty")

                        remoteKeys.prevKey ?: INVALID_PAGE
                    }
                    LoadType.APPEND -> {
                        val remoteKeys = getRemoteKeyForLastItem(state)
                            ?: throw InvalidObjectException("Result is empty")

                        remoteKeys.nextKey ?: INVALID_PAGE
                    }
                }
            }
            .flatMap { page ->
                if (page == INVALID_PAGE) {
                    Single.just(MediatorResult.Success(endOfPaginationReached = true))
                } else {
                    newsAPI.getHeadlines(
                        category = "technology",
                        page = page
                    ).map { mapper.transform(it, isFavourite = false, category = "technology") }
                        .map { insertToDb(page, loadType, it) }
                        .map<MediatorResult> { MediatorResult.Success(endOfPaginationReached = it.endOfPage) }
                        .onErrorReturn { MediatorResult.Error(it) }
                }

            }
            .onErrorReturn { MediatorResult.Error(it) }
    }

    @Suppress("DEPRECATION")
    private fun insertToDb(page: Int, loadType: LoadType, data: NewsModel): NewsModel {
        db.beginTransaction()

        try {
            if (loadType == LoadType.REFRESH) {
                db.newsRemoteKeysDao().clearRemoteKeys()
                db.newsDao().deleteAll()
            }

            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (data.endOfPage) null else page + 1
            val keys = data.news.map {
                NewsModel.NewsRemoteKeys(newsId = it.id, prevKey = prevKey, nextKey = nextKey)
            }
            db.newsRemoteKeysDao().insertAll(keys)
            db.newsDao().insertAllHeadlines(data.news)
            db.setTransactionSuccessful()

        } finally {
            db.endTransaction()
        }

        return data
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Int, NewsModel.News>): NewsModel.NewsRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { news ->
            db.newsRemoteKeysDao().remoteKeysByNewsId(news.id!!)
        }
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, NewsModel.News>): NewsModel.NewsRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { news ->
            db.newsRemoteKeysDao().remoteKeysByNewsId(news.id!!)
        }
    }

    private fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, NewsModel.News>): NewsModel.NewsRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                db.newsRemoteKeysDao().remoteKeysByNewsId(id)
            }
        }
    }

    companion object {
        const val INVALID_PAGE = -1
    }
}