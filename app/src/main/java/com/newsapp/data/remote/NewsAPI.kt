package com.newsapp.data.remote

import com.newsapp.models.NewsResponse
import com.newsapp.utils.ApiConstants
import io.reactivex.Observable
import retrofit2.http.GET

interface NewsAPI {

    @GET(ApiConstants.TOP_HEADLINES)
    fun getHeadlines(): Observable<NewsResponse>
}