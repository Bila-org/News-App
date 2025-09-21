package com.example.newsapp.domain

import androidx.paging.PagingData
import com.example.newsapp.data.local.EntityArticle
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getTopHeadlines(countryCode: String): Flow<PagingData<EntityArticle>>
    fun searchForNews(searchQuery: String): Flow<PagingData<EntityArticle>>
    fun getBookmarkedArticles(): Flow<List<EntityArticle>>
    suspend fun toggleBookmarkArticle(article: EntityArticle)
    suspend fun addToBookmarkArticle(article: EntityArticle)
    suspend fun deleteArticle(article: EntityArticle)
}