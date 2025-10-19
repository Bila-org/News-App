package com.example.newsapp.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.newsapp.data.local.EntityArticle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleList(
    articles: LazyPagingItems<EntityArticle>,
    onArticleClick: (EntityArticle) -> Unit,
    onBookmarkClick: (EntityArticle) -> Unit,
    modifier: Modifier = Modifier
) {

    val loadStates = articles.loadState
    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing = loadStates.refresh is LoadState.Loading

    PullToRefreshBox(
        state = pullToRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = { articles.refresh() },
        modifier = modifier.fillMaxSize(),
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                state = pullToRefreshState
            )
        },
    ) {
        if (articles.itemCount == 0) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        // CircularProgressIndicator()
                    }

                    is LoadState.Error -> {
                        val error = loadStates.refresh as LoadState.Error
                        val errorMessage = error.error.localizedMessage ?: "Unknown error"
                        ErrorItem(
                            errorMessage = "Couldn't load content\n$errorMessage",
                            showRefreshIcon = true,
                            onRetryClick = { articles.retry() },
                            fontSize = 16
                        )
                    }

                    else ->
                        Text("No articles available")
                }
            }

        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (loadStates.prepend is LoadState.Error) {
                    item {
                        val errorMessage =
                            (loadStates.prepend as LoadState.Error).error.localizedMessage
                                ?: "Unknown error"
                        ErrorItem(
                            errorMessage = "Couldn't load previous items\n$errorMessage",
                            onRetryClick = { articles.retry() }
                        )
                    }
                }

                items(
                    articles.itemCount
                ) {
                    articles[it]?.let { article ->
                        NewsCard(
                            article = article,
                            onArticleClick = {
                                onArticleClick(article)
                            },
                            onBookmarkClick = {
                                onBookmarkClick(article)
                            }
                        )
                    }
                }

                if (loadStates.append is LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }


                if (loadStates.append is LoadState.Error) {
                    item {
                        val errorMessage =
                            (loadStates.append as LoadState.Error).error.localizedMessage
                                ?: "Unknown Error"

                        ErrorItem(
                            errorMessage = "Failed to load more content\n$errorMessage",
                            onRetryClick = { articles.retry() }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ArticleList(
    articles: List<EntityArticle>,
    onArticleClick: (EntityArticle) -> Unit,
    onBookmarkClick: (EntityArticle) -> Unit,
    swipeToDelete: (EntityArticle) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,

        ) {
        if (articles.isEmpty()) {
            item {
                Text(
                    text = "No results found",
                    //  modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            itemsIndexed(
                items = articles,
                key = { _, article ->
                    article.url
                }
            ) { index, article ->
                NewsCard(
                    article = article,
                    onArticleClick = {
                        onArticleClick(article)
                    },
                    onBookmarkClick = {
                        onBookmarkClick(article)
                    },
                    swipeToDelete = {
                        swipeToDelete(article)
                    }
                )
            }
        }
    }
}
