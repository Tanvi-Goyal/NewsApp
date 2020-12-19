package com.newsapp.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "news_remote_keys")
data class NewsRemoteKeys(
    @PrimaryKey val newsId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)