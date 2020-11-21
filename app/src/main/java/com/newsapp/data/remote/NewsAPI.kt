package com.newsapp.data.remote

import com.newsapp.models.NewsResponse
import com.newsapp.utils.ApiConstants
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET(ApiConstants.TOP_HEADLINES)
    fun getHeadlines(
        @Query("category") category: String,
        @Query("page") page: Int
    ): Single<NewsResponse>
}