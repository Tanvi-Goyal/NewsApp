package com.newsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.newsapp.data.entities.NewsModel
import com.newsapp.utils.Converters

@Database(
    entities = [NewsModel.News::class, NewsModel.NewsRemoteKeys::class],
    version = 7,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun newsRemoteKeysDao(): NewsRemoteKeysDao
}