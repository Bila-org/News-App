package com.example.newsapp.domain

import androidx.paging.PagingData
import com.example.newsapp.data.dto.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getTopHeadlines(countryCode: String): Flow<PagingData<Article>>
    fun searchForNews(searchQuery: String): Flow<PagingData<Article>>
    fun getBookmarkedArticles(): Flow<List<Article>>
    suspend fun toggleBookmarkArticle(article: Article)
    suspend fun addToBookmarkArticle(article: Article)
    suspend fun deleteArticle(article: Article)
}