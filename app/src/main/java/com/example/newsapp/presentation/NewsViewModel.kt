package com.example.newsapp.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapp.data.Article
import com.example.newsapp.data.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

//data class NewsUiState(
//    val topHeadlines: List<Article> = emptyList(),
//    val bookMarkedArticles: List<Article> = emptyList(),
//    val searchedNews: List<Article> = emptyList(),
//
//    val isLoading: Boolean = false,
//    val errorMessage: String? = null
//)

data class NewsUiState(
    val topHeadlines: Flow<PagingData<Article>>? = null,
    val bookMarkedArticles: List<Article> = emptyList(),
    val searchedNews: Flow<PagingData<Article>>? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null
)


@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(NewsUiState())
    val uiState = _uiState

//    var topHeadlinesPage = 1
//    var searchForNewsPage = 1

    init {
        getTopHeadlines()
        getBookMarkedArticles()
    }

    fun searchForNews(searchQuery: String) {
        if (searchQuery.isEmpty()) return
        val articles = newsRepository.searchForNews(searchQuery)
            .cachedIn(viewModelScope)

        _uiState.value = _uiState.value.copy(
            searchedNews = articles,
            isLoading = false
        )
    }


    fun getTopHeadlines(countryCode: String = "us") {
        val articles = newsRepository.getTopHeadlines(countryCode)
            .cachedIn(viewModelScope)
        _uiState.value = _uiState.value.copy(
            topHeadlines = articles,
            isLoading = false
        )
    }



//    fun getTopHeadlines(countryCode: String = "us") {
//        viewModelScope.launch {
//            _uiState.update {
//                it.copy(
//                    isLoading = true,
//                    topHeadlines = emptyList()
//                )
//            }
//            val result = newsRepository.getTopHeadlines(countryCode, topHeadlinesPage)
//            when (result) {
//                is Resource.Error<*> -> {
//                    _uiState.update {
//                        it.copy(
//                            errorMessage = result.message,
//                            isLoading = false
//                        )
//                    }
//                }
//
//                is Resource.Loading<*> -> {
//                    _uiState.update {
//                        it.copy(
//                            isLoading = true
//                        )
//                    }
//                }
//
//                is Resource.Success<*> -> {
//                    _uiState.update {
//                        it.copy(
//                            topHeadlines = result.data?.articles ?: emptyList(),
//                            isLoading = false
//                        )
//                    }
//                }
//            }
//
//        }
//    }




//    fun searchForNews(searchQuery: String) {
//        if (searchQuery.isEmpty()) return
//        viewModelScope.launch {
//            _uiState.update {
//                it.copy(
//                    isLoading = true,
//                    searchedNews = emptyList()
//                )
//            }
//            val result = newsRepository.searchForNews(searchQuery, searchForNewsPage)
//            when (result) {
//                is Resource.Error<*> -> {
//                    _uiState.update {
//                        it.copy(
//                            errorMessage = result.message,
//                            isLoading = false
//                        )
//                    }
//                }
//
//                is Resource.Loading<*> -> {
//                    _uiState.update {
//                        it.copy(
//                            isLoading = true
//                        )
//                    }
//                }
//
//                is Resource.Success<*> -> {
//                    _uiState.update {
//                        it.copy(
//                            searchedNews = result.data?.articles ?: emptyList(),
//                            isLoading = false
//                        )
//                    }
//                }
//            }
//        }
//    }


    fun getBookMarkedArticles() {
        viewModelScope.launch {
            newsRepository.getBookmarkedArticles().collect { articles ->
                _uiState.value = _uiState.value.copy(
                    bookMarkedArticles = articles
                )
            }
        }
    }

//    fun getBookMarkedArticles() {
//        viewModelScope.launch {
//            newsRepository.getBookmarkedArticles().collect { articles ->
//                _uiState.update {
//                    it.copy(
//                        bookMarkedArticles = articles
//                    )
//                }
//            }
//        }
//    }


    fun toggleBookmarkArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.toggleBookmarkArticle(article)
        }
    }

    fun addToBookmarkArticle(article: Article){
        viewModelScope.launch {
            newsRepository.addToBookmarkArticle(article)
        }
    }


    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.deleteArticle(article)
        }
    }

//    fun clearError() {
//        _uiState.update {
//            it.copy(errorMessage = null)
//        }
//    }
}