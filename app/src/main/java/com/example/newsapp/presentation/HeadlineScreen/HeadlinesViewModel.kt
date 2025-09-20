package com.example.newsapp.presentation.HeadlineScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapp.data.dto.Article
import com.example.newsapp.domain.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class HeadlinesUiState(
    val topHeadlines: Flow<PagingData<Article>>? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)


@HiltViewModel
class HeadlinesViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(HeadlinesUiState())
    val uiState = _uiState


    init {
        getTopHeadlines()
    }


    fun getTopHeadlines(countryCode: String = "us") {
        _uiState.value = _uiState.value.copy(
            isLoading = true
        )
        val articles = newsRepository.getTopHeadlines(countryCode)
            .cachedIn(viewModelScope)
        _uiState.value = _uiState.value.copy(
            topHeadlines = articles,
            isLoading = false
        )
    }

    fun toggleBookmarkArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.toggleBookmarkArticle(article)
        }
    }

    fun addToBookmarkArticle(article: Article) {
        viewModelScope.launch {
            newsRepository.addToBookmarkArticle(article)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }
}