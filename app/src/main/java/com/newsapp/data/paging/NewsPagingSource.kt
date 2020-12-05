package com.newsapp.data.paging

import androidx.paging.PagingSource
import com.newsapp.data.entities.News
import com.newsapp.data.remote.NewsService
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class NewsPagingSource(
    private val newsService: NewsService,
    private val category: String
) : PagingSource<Int, News>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {

        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = newsService.getHeadlines(category, position, params.loadSize)
            val news = response.articles
            LoadResult.Page(
                data = news,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (news.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}