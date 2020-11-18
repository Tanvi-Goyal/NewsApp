package com.newsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.newsapp.data.entities.News
import com.newsapp.utils.Converters

@Database(entities = [News::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class )
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao() : NewsDao
}