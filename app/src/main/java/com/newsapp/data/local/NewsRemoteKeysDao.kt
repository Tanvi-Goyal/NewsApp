package com.newsapp.data.local

import com.newsapp.data.entities.NewsRemoteKeys
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<NewsRemoteKeys>)

    @Query("SELECT * FROM news_remote_keys WHERE newsId = :newsId")
    suspend fun remoteKeysRepoId(newsId: Int): NewsRemoteKeys?

    @Query("DELETE FROM news_remote_keys")
    suspend fun clearRemoteKeys()
}