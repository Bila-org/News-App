package com.example.newsapp.data.repository

import android.content.SharedPreferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsapp.data.dto.Article
import com.example.newsapp.data.local.ArticleDatabase
import com.example.newsapp.data.paging.SearchNewsPagingSource
import com.example.newsapp.data.paging.TopHeadlinesPagingSource
import com.example.newsapp.domain.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NewsRepositoryImp(
    val db: ArticleDatabase,
    private val sharedPreferences: SharedPreferences
) : NewsRepository {

    companion object {
        private const val CACHE_DURATION_MS = 60 * 60 * 1000 // 1 hour
        private const val PREFS_KEY_LAST_FETCH = "lastFetchTime"
    }

    override fun getTopHeadlines(
        countryCode: String
    ): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                TopHeadlinesPagingSource(
                    countryCode = countryCode
                )
            }
        ).flow
    }


    override fun searchForNews(
        searchQuery: String,
    ): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                SearchNewsPagingSource(
                    searchQuery = searchQuery
                )
            }
        ).flow
    }


    override fun getBookmarkedArticles(): Flow<List<Article>> {
        return db.getArticleDao().getBookmarkedArticles()
    }

    override suspend fun toggleBookmarkArticle(article: Article) {
        withContext(Dispatchers.IO) {
            db.getArticleDao().upsert(article.copy(isBookmarked = !article.isBookmarked))
        }
    }

    override suspend fun addToBookmarkArticle(article: Article) {
        withContext(Dispatchers.IO) {
            db.getArticleDao().upsert(article.copy(isBookmarked = true))
        }
    }


    override suspend fun deleteArticle(article: Article) {
        withContext(Dispatchers.IO)
        {
            db.getArticleDao().deleteArticle(article)
        }
    }


    private fun shouldFetchFromApi(): Boolean {
        val lastFetchTime = sharedPreferences.getLong(PREFS_KEY_LAST_FETCH, 0)
        val currentTime = System.currentTimeMillis()
        return (currentTime - lastFetchTime).compareTo(CACHE_DURATION_MS) > 0
    }
}