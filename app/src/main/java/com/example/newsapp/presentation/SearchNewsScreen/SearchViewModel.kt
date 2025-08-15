package com.example.newsapp.presentation.SearchNewsScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapp.data.dto.Article
import com.example.newsapp.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject



data class SearchUiState(
    val searchedNews: Flow<PagingData<Article>>? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel()  {

    private val _uiState = mutableStateOf(SearchUiState())
    val uiState = _uiState

    fun searchForNews(searchQuery: String) {
        if (searchQuery.isEmpty()) return
        val articles = newsRepository.searchForNews(searchQuery)
            .cachedIn(viewModelScope)

        _uiState.value = _uiState.value.copy(
            searchedNews = articles,
            isLoading = false
        )
    }

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


//    fun clearError() {
//        _uiState.update {
//            it.copy(errorMessage = null)
//        }
//    }
}