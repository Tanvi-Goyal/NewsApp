package com.newsapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.newsapp.data.entities.NewsModel
import io.reactivex.Observable

@Dao
interface NewsDao {

    /*
     * IGNORE if same news coming based on URL (If REPLACED: bookmarks will be cleared bcz of default 0 column value)
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllHeadlines(news: List<NewsModel.News>)

    @Query("SELECT * FROM news WHERE category = :category order by publishedAt desc")
    fun getAllHeadlines(category: String): PagingSource<Int, NewsModel.News>

    @Query("UPDATE news SET isFavorite = :isFavorite WHERE id = :id")
    fun addToFavorites(id: Int, isFavorite: Int): Int

    @Query("SELECT * FROM news WHERE isFavorite = '1' AND category = :category")
    fun getFavoriteNews(category: String): Observable<List<NewsModel.News>>

    @Query("DELETE FROM news")
    fun deleteAll()
}