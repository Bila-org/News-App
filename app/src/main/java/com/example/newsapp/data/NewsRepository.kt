package com.example.newsapp.data

import android.content.SharedPreferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsapp.data.db.ArticleDatabase
import com.example.newsapp.data.remote.SearchNewsPagingSource
import com.example.newsapp.data.remote.TopHeadlinesPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NewsRepository(
    val db: ArticleDatabase,
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val CACHE_DURATION_MS = 60 * 60 * 1000 // 1 hour
        private const val PREFS_KEY_LAST_FETCH = "lastFetchTime"
    }

    fun getTopHeadlines(
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


    fun searchForNews(
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



    fun getBookmarkedArticles(): Flow<List<Article>> {
        return db.getArticleDao().getBookmarkedArticles()
    }

    suspend fun toggleBookmarkArticle(article: Article) {
        withContext(Dispatchers.IO) {
            db.getArticleDao().upsert(article.copy(isBookmarked = !article.isBookmarked))
        }
    }

    suspend fun addToBookmarkArticle(article: Article){
        withContext(Dispatchers.IO){
            db.getArticleDao().upsert(article.copy(isBookmarked = true))
        }
    }


    suspend fun deleteArticle(article: Article) {
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




//    suspend fun getTopHeadlines(
//        countryCode: String,
//        pageNumber: Int
//    ): Resource<NewsResponse> {
//
//        return withContext(Dispatchers.IO) {
//            if (shouldFetchFromApi()) {
//                try {
//                    val response = RetrofitInstance.api.fetchTopHeadlines(countryCode, pageNumber)
//                    if (response.isSuccessful) {
//                        response.body()?.let { resultResponse ->
//
//                            try {
//                                db.getArticleDao().deleteNonBookmarkArticles()
//                                db.getArticleDao().upsertAll(resultResponse.articles)
//
//                                sharedPreferences.edit {
//                                    putLong(
//                                        PREFS_KEY_LAST_FETCH,
//                                        System.currentTimeMillis()
//                                    )
//                                }
//
//                                Log.d("Debug", "Successfully save articles")
//                            } catch (e: Exception) {
//                                Log.d("Debug", "Error saving articles: ${e}")
//                            }
//
//                            Resource.Success(resultResponse)
//                        } ?: Resource.Error("No data received from API")
//                    } else {
//                        Resource.Error(response.message())
//                    }
//                } catch (e: Exception) {
//                    val cachedArticles = db.getArticleDao().getAllArticles().first()
//                    val cachedResponse = NewsResponse(
//                        articles = cachedArticles,
//                        status = "ok",
//                        totalResults = cachedArticles.count()
//                    )
//                    if (cachedResponse.totalResults > 0) {
//                        Resource.Success(cachedResponse)
//                    } else {
//                        Resource.Error("No internet connection and no recent cached data")
//                    }
//                }
//            } else {
//                val cachedArticles = db.getArticleDao().getAllArticles().first()
//                val cachedResponse = NewsResponse(
//                    articles = cachedArticles,
//                    status = "ok",
//                    totalResults = cachedArticles.count()
//                )
//                Resource.Success(cachedResponse)
//                if (cachedResponse.totalResults > 0) {
//                    Resource.Success(cachedResponse)
//                } else {
//                    Resource.Error("No recent cached data")
//                }
//
//            }
//        }
//    }
//

//    suspend fun searchForNews(
//        searchQuery: String,
//        pageNumber: Int
//    ): Resource<NewsResponse> {
//        return withContext(Dispatchers.IO) {
//            try {
//                val response = RetrofitInstance.api.searchForNews(searchQuery, pageNumber)
//                if (response.isSuccessful) {
//                    response.body()?.let { resultResponse ->
//                        Resource.Success(resultResponse)
//
//                    } ?: Resource.Error("No data received from API")
//                } else {
//                    Resource.Error(response.message())
//                }
//            } catch (e: Exception) {
//                Resource.Error("No internet connection")
//            }
//
//        }
//    }

}