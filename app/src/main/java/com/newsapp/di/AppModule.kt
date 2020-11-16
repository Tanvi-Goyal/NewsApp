package com.newsapp.di

import android.content.Context
import androidx.room.Room
import com.newsapp.data.local.AppDatabase
import com.newsapp.data.local.NewsDao
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
        ).build()

    @Singleton
    @Provides
    fun providesNewsDao(db: AppDatabase): NewsDao = db.newsDao()
}