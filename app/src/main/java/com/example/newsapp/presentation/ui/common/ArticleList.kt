package com.example.newsapp.presentation.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.newsapp.data.Article


@Composable
fun ArticleList(
    articles: LazyPagingItems<Article>,
    onArticleClick: (Article) -> Unit,
    onBookmarkClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    val handlePagingResult = handlePagingResult(articles)

    if (handlePagingResult) {
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
    }
}


fun handlePagingResult(articles: LazyPagingItems<Article>): Boolean {
    val loadState = articles.loadState
    val error = when {
        loadState.refresh is LoadState.Error ->
            loadState.refresh as LoadState.Error

        loadState.prepend is LoadState.Error ->
            loadState.prepend as LoadState.Error

        loadState.append is LoadState.Error ->
            loadState.append as LoadState.Error

        else -> null
    }

    return when {
        error != null -> {
            false
        }

        else -> {
            true
        }
    }
}


@Composable
fun ArticleList(
    articles: List<Article>,
    onArticleClick: (Article) -> Unit,
    onBookmarkClick: (Article) -> Unit,
    swipeToDelete: (Article) -> Unit = {},
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
                key = {_ , article ->
                    article.url
                }
//                count = articles.count(),
//                key = {
//                    articles.indexOf(articles[it])
//                }
            ) { index, article ->
                NewsCard(
                    article = articles[index],
                    onArticleClick = {
                        onArticleClick(articles[index])
                    },
                    onBookmarkClick = {
                        onBookmarkClick(articles[index])
                    },
                    swipeToDelete = {
                        swipeToDelete(articles[index])
                    }
                )
            }
        }
    }
}



