package com.newsapp.di

import com.newsapp.data.local.NewsDao
import com.newsapp.data.remote.NewsAPI
import com.newsapp.data.repositories.NewsRepository
import com.newsapp.utils.ApiConstants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {
    @Provides
    fun provideBaseUrl(): String {
        return ApiConstants.BASE_URL
    }

    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()

        // Add API KEY and Constant Query Params to all requests
        okHttpClient.addInterceptor(logger).addInterceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url
            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("apiKey", ApiConstants.API_KEY)
                .addQueryParameter("country", ApiConstants.NEWS_COUNTRY)
                .addQueryParameter("pageSize", ApiConstants.RESULTS_PER_PAGE)
                .build()
            val requestBuilder = original.newBuilder().url(url).build()
            chain.proceed(requestBuilder)
        }

        return okHttpClient.build()
    }

    @Provides
    fun providesConverterFactory(): Converter.Factory =
        MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build())

    @Provides
    fun providesRetrofit(
        baseUrl: String,
        converterFactory: Converter.Factory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient).build()
    }

    @Provides
    fun providesNewsApi(retrofit: Retrofit): NewsAPI = retrofit.create(NewsAPI::class.java)

    @Singleton
    @Provides
    fun providesNewsRepo(local: NewsDao, remote: NewsAPI): NewsRepository = NewsRepository(
        local,
        remote
    )
}