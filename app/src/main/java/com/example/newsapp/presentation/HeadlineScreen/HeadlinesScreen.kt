package com.example.newsapp.presentation.HeadlineScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavBackStackEntry
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapp.data.local.EntityArticle
import com.example.newsapp.presentation.common.components.ArticleList

@Composable
fun HeadlinesNewsScreen(
    viewModel: HeadlinesViewModel,
    onArticleClick: (EntityArticle) -> Unit,
    onBookmarkClick: (EntityArticle) -> Unit,
    modifier: Modifier = Modifier
) {

    val uiState = viewModel.uiState.value

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        uiState.topHeadlines?.let {
            val articles = it.collectAsLazyPagingItems()
            ArticleList(
                articles = articles,
                onArticleClick = onArticleClick,
                onBookmarkClick = onBookmarkClick
            )
        }
    }
}