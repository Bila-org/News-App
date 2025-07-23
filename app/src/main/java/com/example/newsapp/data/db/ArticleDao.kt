package com.example.newsapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.newsapp.data.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Upsert()
    suspend fun upsert(article: Article): Long

    @Upsert()
    suspend fun upsertAll(articles: List<Article>)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<Article>>


    @Query("SELECT * FROM articles WHERE isBookmarked = 1")
    fun getBookmarkedArticles(): Flow<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("DELETE FROM articles WHERE isBookmarked = 0")
    suspend fun deleteNonBookmarkArticles()
}