package com.newsapp.data.paging

import com.newsapp.data.entities.NewsRemoteKeys
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.newsapp.data.entities.News
import com.newsapp.data.local.AppDatabase
import com.newsapp.data.remote.NewsService
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

private const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val category: String,
    private val service: NewsService,
    private val db: AppDatabase
) : RemoteMediator<Int, News>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, News>): MediatorResult {

        val page = when(loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                if (remoteKeys == null) {
                    throw InvalidObjectException("Remote key and the prevKey should not be null")
                }
                val prevKey = remoteKeys.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = false)
                }
                remoteKeys.prevKey
            }
            LoadType.APPEND -> {

                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys == null || remoteKeys.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }
        }

        try {
            val apiResponse = service.getHeadlines(category, page, state.config.pageSize)
            val newsList = apiResponse.articles
            val endOfPagination = newsList.isEmpty()

            db.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().clearRemoteKeys()
                    db.newsDao().deleteAll()
                }

                val prevKey = if (page == STARTING_PAGE_INDEX) null else (page - 1)
                val nextKey = if (endOfPagination) null else page + 1

                val keys = newsList.map {
                    NewsRemoteKeys(newsId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                db.remoteKeysDao().insertAll(keys)
                db.newsDao().insertAllHeadlines(newsList)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPagination)
        }catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, News>
    ): NewsRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                db.remoteKeysDao().remoteKeysRepoId(repoId)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, News>): NewsRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                repo.id?.let { db.remoteKeysDao().remoteKeysRepoId(it) }
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, News>) : NewsRemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                repo.id?.let { db.remoteKeysDao().remoteKeysRepoId(it) }
            }
    }
}