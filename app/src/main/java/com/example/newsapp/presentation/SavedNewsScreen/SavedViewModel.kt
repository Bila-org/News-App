package com.example.newsapp.presentation.SavedNewsScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.dto.Article
import com.example.newsapp.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SavedUiState(
    val bookMarkedArticles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)


@HiltViewModel
class SavedViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(SavedUiState())
    val uiState = _uiState


    init {
        getBookMarkedArticles()
    }

    fun getBookMarkedArticles() {
        viewModelScope.launch {
            newsRepository.getBookmarkedArticles().collect { articles ->
                _uiState.value = _uiState.value.copy(
                    bookMarkedArticles = articles
                )
            }
        }
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