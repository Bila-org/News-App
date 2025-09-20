package com.example.newsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.data.dto.Article
import com.example.newsapp.data.local.dao.ArticleDao

@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

}