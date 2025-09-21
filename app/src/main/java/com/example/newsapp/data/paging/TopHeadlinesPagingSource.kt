package com.example.newsapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapp.data.dto.Article
import com.example.newsapp.data.local.ArticleDatabase
import com.example.newsapp.data.local.EntityArticle
import com.example.newsapp.data.remote.RetrofitInstance
import com.example.newsapp.data.mapper.ArticleMapper
import kotlin.math.ceil

class TopHeadlinesPagingSource(
    private val countryCode: String = "us",
    private val db: ArticleDatabase,
) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)
                ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        return try {
            val response = RetrofitInstance.Companion.api.fetchTopHeadlines(countryCode, page)
            if (response.isSuccessful) {
                response.body()?.let { newsResponse ->
                    val articles = newsResponse.articles.distinctBy { it.url }

                    val entityArticles = articles.map { article ->
                        val existing = db.getArticleDao().getArticleByUrl(article.url)
                        if (existing != null) {
                            EntityArticle(
                                id = existing.id,
                                author = article.author,
                                content = article.content,
                                description = article.description,
                                publishedAt = article.publishedAt,
                                source = article.source,
                                title = article.title,
                                url = article.url,
                                urlToImage = article.urlToImage,
                                isBookmarked = existing.isBookmarked
                            )
                        } else {
                            ArticleMapper.toEntity(article)
                        }
                    }

                    try {
                        // Only delete non-bookmarked articles on the first page (initial load/refresh)
                        if (page == 1) {
                            db.getArticleDao().deleteNonBookmarkArticles()
                        }
                        db.getArticleDao().upsertAll(entityArticles)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    val totalResult = newsResponse.totalResults
                    val totalPages = ceil(
                        (totalResult.toDouble() / params.loadSize)
                    ).toInt()

                    LoadResult.Page(
                        data = articles,
                        prevKey = if (page > 1) page - 1 else null,
                        nextKey = if (page < totalPages) page + 1 else null
                    )
                } ?: LoadResult.Error(Exception("No data received from API"))
            } else {
                LoadResult.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}