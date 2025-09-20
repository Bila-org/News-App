package com.example.newsapp.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.newsapp.data.local.ArticleDatabase
import com.example.newsapp.data.repository.NewsRepositoryImp
import com.example.newsapp.domain.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideArticleDatabase(app: Application): ArticleDatabase {
        return Room.databaseBuilder(
            app,
            ArticleDatabase::class.java,
            "article_database"
        )
//            .addTypeConverter(Converters())
//            .fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("news_app_preferences", Context.MODE_PRIVATE)
    }


    @Provides
    @Singleton
    fun provideNewsRepository(
        db: ArticleDatabase,
        sharedPreferences: SharedPreferences
    ): NewsRepository {
        return NewsRepositoryImp(db, sharedPreferences)
    }


    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
}