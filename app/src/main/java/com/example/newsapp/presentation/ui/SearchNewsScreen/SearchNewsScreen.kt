package com.example.newsapp.presentation.ui.SearchNewsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapp.data.Article
import com.example.newsapp.presentation.NewsUiState
import com.example.newsapp.presentation.ui.common.ArticleList

@Composable
fun SearchNewsScreen(
    uiState: NewsUiState,
    onArticleClick: (Article) -> Unit,
    onBookmarkClick: (Article) -> Unit,
    onSearchQuery: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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


//        Column(
//            modifier = modifier
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text(
//                text = "Search News",
//                style = MaterialTheme.typography.displayMedium
//            )
//        }


//    @Preview(
//        showBackground = true,
//        showSystemUi = true
//    )
//    @Composable
//    fun SearchNewsPreview() {
//        SearchNews()
//    }