package com.newsapp.data.remote

import com.newsapp.models.NewsResponse
import com.newsapp.utils.ApiConstants
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET(ApiConstants.TOP_HEADLINES)
    fun getHeadlines(
        @Query("category") category: String
    ): Observable<NewsResponse>
}