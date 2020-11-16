package com.newsapp.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// URL to check Unique news
@Entity(tableName = "news", indices = [Index(value = ["url"], unique = true)])
data class News(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val source: Source,
    val title: String,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
    var isBookmarked: Boolean = false
)

data class Source(
    val id: Any? = null,
    val name: String
)