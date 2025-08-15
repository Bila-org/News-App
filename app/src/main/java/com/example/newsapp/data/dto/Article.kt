package com.example.newsapp.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "articles"
)
data class Article(
    val author: String? = null,
    val content: String? = null,
    val description: String? = null,
    val publishedAt: String? = null,

    val source: Source,
    val title: String? = null,

    @PrimaryKey(autoGenerate = false)
    val url: String,
    val urlToImage: String? = null,

    val isBookmarked: Boolean = false   // For favorite logic implementation
)