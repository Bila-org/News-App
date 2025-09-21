package com.example.newsapp.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.LoadType
import androidx.paging.compose.LazyPagingItems
import com.example.newsapp.data.dto.Article
import com.example.newsapp.data.local.EntityArticle


@Composable
fun ArticleList(
    articles: LazyPagingItems<EntityArticle>,
    onArticleClick: (EntityArticle) -> Unit,
    onBookmarkClick: (EntityArticle) -> Unit,
    modifier: Modifier = Modifier
) {

    var errorType: LoadType? = null
    var errorMessage: String? = null

    handlePagingResult(articles) { error, type ->
        errorType = type
        errorMessage = error.localizedMessage
    }

    if (errorType == null) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

            ) {
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
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Refresh error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            when (errorType) {
                LoadType.REFRESH -> {
                    Text(
                        text = "Unable to refresh content",//\n$errorMessage",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }

                LoadType.PREPEND -> {
                    Text(
                        text = "Couldn't load previous items",//\n$errorMessage",
                        color = Color(0xFFF57C00), // Orange color for warning
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        fontStyle = FontStyle.Italic
                    )
                }

                LoadType.APPEND -> {
                    Text(
                        text = "Failed to load more content",//\n$errorMessage",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    articles.retry()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Retry")
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
        modifier = Modifier
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

fun handlePagingResult(
    articles: LazyPagingItems<EntityArticle>,
    onError: (Throwable, LoadType) -> Unit
) {
    val loadState = articles.loadState
    listOf(
        LoadType.REFRESH to loadState.refresh,
        LoadType.PREPEND to loadState.prepend,
        LoadType.APPEND to loadState.append
    ).forEach { (type, state) ->
        if (state is LoadState.Error) {
            onError(state.error, type)
        }
    }
}

fun handlePagingResult(articles: LazyPagingItems<Article>): Boolean {
    val loadState = articles.loadState
    return listOf(
        loadState.refresh,
        loadState.prepend,
        loadState.append
    ).filterIsInstance<LoadState.Error>().isEmpty()
}
