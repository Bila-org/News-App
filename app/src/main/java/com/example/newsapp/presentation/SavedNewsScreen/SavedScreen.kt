package com.example.newsapp.presentation.SavedNewsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.newsapp.data.dto.Article
import com.example.newsapp.presentation.common.components.ArticleList

@Composable
fun SavedNewsScreen(
    uiState: SavedUiState,
    onArticleClick: (Article) -> Unit,
    onBookmarkClick: (Article) -> Unit,
    swipeToDelete: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ArticleList(
            articles = uiState.bookMarkedArticles,
            onArticleClick = onArticleClick,
            onBookmarkClick = onBookmarkClick,
            swipeToDelete = swipeToDelete,
        )
    }
}