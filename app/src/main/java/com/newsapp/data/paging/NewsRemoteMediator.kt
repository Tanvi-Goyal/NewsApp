package com.newsapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.newsapp.data.entities.News
import com.newsapp.data.local.AppDatabase
import com.newsapp.data.remote.NewsService

//@ExperimentalPagingApi
//class NewsRemoteMediator(
//    private val category: String,
//    private val service: NewsService,
//    private val db: AppDatabase
//) : RemoteMediator<Int, News>() {
//    override suspend fun load(loadType: LoadType, state: PagingState<Int, News>): MediatorResult {
//
//    }
//}