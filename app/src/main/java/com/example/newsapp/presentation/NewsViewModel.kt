package com.example.newsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.Article
import com.example.newsapp.data.NewsRepository
import com.example.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NewsUiState(
    val topHeadlines: List<Article> = emptyList(),
    val bookMarkedArticles: List<Article> = emptyList(),
    val searchedNews: List<Article> = emptyList(),

    val isLoading: Boolean = false,
    val errorMessage: String? = null
)


@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState = _uiState.asStateFlow()

    var topHeadlinesPage = 1
    var searchForNewsPage = 1

    init {
        getTopHeadlines()
        getBookMarkedArticles()
    }

    fun getTopHeadlines(countryCode: String = "us") {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    topHeadlines = emptyList()
                )
            }
            val result = newsRepository.getTopHeadlines(countryCode, topHeadlinesPage)
            when (result) {
                is Resource.Error<*> -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.message,
                            isLoading = false
                        )
                    }
                }

                is Resource.Loading<*> -> {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }

                is Resource.Success<*> -> {
                    _uiState.update {
                        it.copy(
                            topHeadlines = result.data?.articles ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
            }

        }
    }


    fun searchForNews(searchQuery: String) {
        if (searchQuery.isEmpty()) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    searchedNews = emptyList()
                )
            }
            val result = newsRepository.searchForNews(searchQuery, searchForNewsPage)
            when (result) {
                is Resource.Error<*> -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.message,
                            isLoading = false
                        )
                    }
                }

                is Resource.Loading<*> -> {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }

                is Resource.Success<*> -> {
                    _uiState.update {
                        it.copy(
                            searchedNews = result.data?.articles ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }


    fun getBookMarkedArticles() {
        viewModelScope.launch {
            newsRepository.getBookmarkedArticles().collect { articles ->
                _uiState.update {
                    it.copy(
                        bookMarkedArticles = articles
                    )
                }
            }
        }
    }


    fun toggleBookmarkArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.toggleBookmarkArticle(article)
        }
    }


    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.deleteArticle(article)
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }
}