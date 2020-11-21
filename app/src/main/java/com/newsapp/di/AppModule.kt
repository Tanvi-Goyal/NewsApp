package com.newsapp.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.newsapp.data.local.AppDatabase
import com.newsapp.data.local.NewsDao
import com.newsapp.data.local.NewsRemoteKeysDao
import com.newsapp.data.remote.NewsAPI
import com.newsapp.data.repositories.GetNewsRepositoryImpl
import com.newsapp.data.repositories.NewsRemoteMediator
import com.newsapp.models.NewsMapper
import com.newsapp.utils.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    fun providesContext(@ApplicationContext applicationContext: Context): Context {
        return applicationContext
    }

    @Singleton
    @Provides
    fun provideDatabase(appContext: Context) =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            AppConstants.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun providesNewsDao(db: AppDatabase): NewsDao = db.newsDao()

    @Singleton
    @Provides
    fun providesNewsRemoteKeysDao(db: AppDatabase): NewsRemoteKeysDao = db.newsRemoteKeysDao()

    @Provides
    fun providesNewsMapper() = NewsMapper()

    @Singleton
    @Provides
    fun providesNewsMediator(
        newsAPI: NewsAPI,
        db: AppDatabase,
        mapper: NewsMapper
    ): NewsRemoteMediator =
        NewsRemoteMediator(newsAPI, db, mapper)

    @Provides
    fun providesNewsRepo(
        db: AppDatabase,
        remoteMediator: NewsRemoteMediator
    ): GetNewsRepositoryImpl = GetNewsRepositoryImpl(db, remoteMediator)

}