package com.newsapp.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsModel(
    val total: Int = 0,
    val page: Int = 0,
    val news: List<News>
) : Parcelable {

    @IgnoredOnParcel
    val endOfPage = total == page

    @Parcelize
    @Entity(tableName = "news", indices = [Index(value = ["url"], unique = true)])
    data class News(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val author: String?,
        val title: String,
        val description: String?,
        val url: String?,
        val urlToImage: String?,
        val publishedAt: String?,
        val content: String?,
        var isFavorite: Boolean = false,
        var category: String = ""
    ) : Parcelable

    @Entity(tableName = "news_remote_keys")
    data class NewsRemoteKeys(
        @PrimaryKey val newsId: Int,
        val prevKey: Int?,
        val nextKey: Int?
    )
}