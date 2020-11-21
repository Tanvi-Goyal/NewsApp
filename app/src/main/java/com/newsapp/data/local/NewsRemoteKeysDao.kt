package com.newsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.newsapp.data.entities.NewsModel

@Dao
interface NewsRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remoteKey: List<NewsModel.NewsRemoteKeys>)

    @Query("SELECT * FROM news_remote_keys WHERE newsId = :newsId")
    fun remoteKeysByNewsId(newsId: Int): NewsModel.NewsRemoteKeys?

    @Query("DELETE FROM news_remote_keys")
    fun clearRemoteKeys()
}