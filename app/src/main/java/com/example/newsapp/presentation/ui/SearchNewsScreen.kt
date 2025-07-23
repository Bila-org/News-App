package com.example.newsapp.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.newsapp.data.Article
import com.example.newsapp.presentation.NewsViewModel

@Composable
fun SearchNewsScreen(
    viewModel: NewsViewModel,
    onArticleClick: (Article) -> Unit,
    onBookmarkClick: (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val articles = uiState.searchedNews

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SearchBar(
            onSearch = {
                viewModel.searchForNews(it)
            }
        )

        if (uiState.isLoading) {
            Spacer(modifier = Modifier.weight(1f))

            CircularProgressIndicator(
                //modifier = Modifier.
            )
            Spacer(modifier = Modifier.weight(1f))
        } else {
            uiState.errorMessage?.let { message ->
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = message ?: "An error occurred",
                    //modifier = Modifier.align(Alignment.Center)
                )
                Spacer(modifier = Modifier.weight(1f))
            }

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