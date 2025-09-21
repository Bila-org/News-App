package com.example.newsapp.data.local.dao


import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.newsapp.data.local.EntityArticle
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Upsert()
    suspend fun upsert(article: EntityArticle)

    @Upsert()
    suspend fun upsertAll(articles: List<EntityArticle>)

    @Query("SELECT * FROM articles ORDER BY id ASC")
    fun pagingSource(): PagingSource<Int, EntityArticle>

    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<EntityArticle>>

    @Query("SELECT * FROM articles WHERE isBookmarked = 1")
    fun getBookmarkedArticles(): Flow<List<EntityArticle>>

    @Delete
    suspend fun deleteArticle(article: EntityArticle)

    @Query("DELETE FROM articles WHERE isBookmarked = 0")
    suspend fun deleteNonBookmarkArticles()

    @Query("SELECT * FROM articles WHERE url = :url LIMIT 1")
    suspend fun getArticleByUrl(url: String): EntityArticle?
}