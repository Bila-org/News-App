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
import com.example.newsapp.data.dto.Article
import com.example.newsapp.presentation.common.components.ArticleList

@Composable
fun HeadlinesNewsScreen(
    viewModel: HeadlinesViewModel,
    backStackEntry: NavBackStackEntry,
    onArticleClick: (Article) -> Unit,
    onBookmarkClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {

    val uiState = viewModel.uiState.value
//    val articles = uiState.topHeadlines

    DisposableEffect(backStackEntry) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.getTopHeadlines()
            }
        }
        backStackEntry.lifecycle.addObserver(observer)
        onDispose { backStackEntry.lifecycle.removeObserver(observer) }
    }

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