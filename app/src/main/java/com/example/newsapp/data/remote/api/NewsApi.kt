package com.example.newsapp.data.remote.api

import com.example.newsapp.BuildConfig
import com.example.newsapp.data.dto.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun fetchTopHeadlines(
        @Query("country") countryCode: String = "us",
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Response<NewsResponse>


    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int = 1,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Response<NewsResponse>
}