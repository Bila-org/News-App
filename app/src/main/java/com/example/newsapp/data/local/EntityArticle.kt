package com.example.newsapp.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.newsapp.data.dto.Source


@Entity(
    tableName = "articles",
    indices = [Index(value = ["url"], unique = true)]
)
data class EntityArticle(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val author: String? = null,
    val content: String? = null,
    val description: String? = null,
    val publishedAt: String? = null,
    val source: Source,
    val title: String? = null,
    val url: String,
    val urlToImage: String? = null,
    val isBookmarked: Boolean = false   // For favorite logic implementation
)