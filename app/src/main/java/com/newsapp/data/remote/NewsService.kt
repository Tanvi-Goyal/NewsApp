package com.newsapp.data.remote

import com.newsapp.models.NewsResponse
import com.newsapp.utils.ApiConstants
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET(ApiConstants.TOP_HEADLINES)
    suspend fun getHeadlines(
        @Query("category") category: String,
        @Query("page") page: Int,
        @Query("pageSize") Int: Int
    ): NewsResponse
}