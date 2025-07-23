package com.example.newsapp.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavBackStackEntry
import com.example.newsapp.data.Article
import com.example.newsapp.presentation.NewsViewModel

@Composable
fun HeadlinesNewsScreen(
    viewModel: NewsViewModel,
    backStackEntry: NavBackStackEntry,
    onArticleClick: (Article) -> Unit,
    onBookmarkClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsState()
    val articles = uiState.topHeadlines

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
        if (uiState.isLoading) {
            Spacer(modifier = Modifier.weight(1f))

            CircularProgressIndicator(
                //modifier = Modifier.
            )
            Spacer(modifier = Modifier.weight(1f))
        } else {
            uiState.errorMessage?.let {
                uiState.errorMessage?.let { message ->
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = message ?: "An error occurred",
                        //modifier = Modifier.align(Alignment.Center)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                items(
                    articles.count()
                ) {
                    NewsCard(
                        article = articles[it],
                        onArticleClick = {
                            onArticleClick(articles[it])
                        },
                        onBookmarkClick = {
                            onBookmarkClick(articles[it])
                        }
                    )
                }
            }
        }
    }
}


//@Preview(
//    showBackground = true,
//    showSystemUi = true
//)
//@Composable
//fun HeadlinesNewsPreview() {
//    HeadlinesNews()
//}