package com.newsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.newsapp.data.entities.News
import io.reactivex.Observable

@Dao
interface NewsDao {

    /*
     * IGNORE if same news coming based on URL (If REPLACED: bookmarks will be cleared bcz of default 0 column value)
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllHeadlines(news: List<News>)

    @Query("SELECT * FROM news WHERE category = :category order by publishedAt desc")
    fun getAllHeadlines(category: String): Observable<List<News>>

    @Query("UPDATE news SET isFavorite = :isFavorite WHERE id = :id")
    fun addToFavorites(id: Int, isFavorite: Int): Int

    @Query("SELECT * FROM news WHERE isFavorite = '1' AND category = :category")
    fun getFavoriteNews(category: String): Observable<List<News>>

    @Query("DELETE FROM news")
    fun deleteAll()
}