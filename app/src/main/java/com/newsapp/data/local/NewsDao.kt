package com.newsapp.data.local

import androidx.lifecycle.LiveData
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

    @Query("SELECT * FROM news order by publishedAt desc")
    fun getAllHeadlines(): Observable<List<News>>
}