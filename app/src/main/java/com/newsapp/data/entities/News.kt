package com.newsapp.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "news", indices = [Index(value = ["url"], unique = true)])
data class News(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
    var isFavorite: Boolean = false,
    var category : String = ""
)

data class Source(
    val id: Any? = null,
    val name: String
)