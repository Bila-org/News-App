package com.example.newsapp.presentation.SearchNewsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapp.data.local.EntityArticle
import com.example.newsapp.presentation.common.components.ArticleList

@Composable
fun SearchNewsScreen(
    uiState: SearchUiState,
    onArticleClick: (EntityArticle) -> Unit,
    onBookmarkClick: (EntityArticle) -> Unit,
    onSearchQuery: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        SearchBar(
            onSearch = {
                onSearchQuery(it)
            }
        )
        uiState.searchedNews?.let {
            val articles = it.collectAsLazyPagingItems()
            ArticleList(
                articles = articles,
                onArticleClick = onArticleClick,
                onBookmarkClick = onBookmarkClick
            )
        }
    }
}