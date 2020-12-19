package com.newsapp.data.local

import com.newsapp.data.entities.NewsRemoteKeys
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.newsapp.data.entities.News
import com.newsapp.utils.Converters

@Database(
    entities = [News::class, NewsRemoteKeys::class],
    version = 11,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}