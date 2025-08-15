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

//    companion object {
//        @Volatile
//        private var instance: ArticleDatabase? = null
//        private val LOCK = Any()
//        @OptIn(InternalCoroutinesApi::class)
//        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
//            instance ?: createDatabase(context).also { instance = it }
//        }
//
//        private fun createDatabase(context: Context) =
//            Room.databaseBuilder(
//                context.applicationContext,
//                ArticleDatabase::class.java,
//                "article_database"
//            ).build()
//    }
}