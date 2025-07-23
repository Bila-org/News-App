package com.example.newsapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.presentation.NewsViewModel
import com.example.newsapp.presentation.Screen
import com.example.newsapp.presentation.ui.HeadlinesNewsScreen
import com.example.newsapp.presentation.ui.SavedNewsScreen
import com.example.newsapp.presentation.ui.SearchNewsScreen
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.util.OpenArticleInBrowser

class NewsApp {
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsAppMain(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Newsroom",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            AppBottomNavigationBar(
                onClick = {
                    navController.navigate(it.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navController = navController
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Headlines.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Headlines.route) { backStackEntry ->
                val viewModel: NewsViewModel = hiltViewModel(backStackEntry)
                val context = LocalContext.current
                HeadlinesNewsScreen(
                    viewModel,
                    backStackEntry,
                    onArticleClick = { article ->
                        OpenArticleInBrowser.launch(context, article.url)
                        /*  // Code for calling WebViewScreen from the HeadlinesNewsScreen
                        article ->
                        val encodedUrl =
                            URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
                        navController.navigate(
                            "${Screen.DetailNews.route}/${encodedUrl}"
                        )
                        */
                    },
                    onBookmarkClick = { article ->
                        viewModel.toggleBookmarkArticle(article)
                    }
                )
            }

            composable(Screen.Saved.route) {
                val viewModel: NewsViewModel = hiltViewModel()
                val context = LocalContext.current
                SavedNewsScreen(
                    viewModel,
                    onArticleClick = { article ->
                        OpenArticleInBrowser.launch(context, article.url)
                    },
                    onBookmarkClick = { article ->
                        viewModel.toggleBookmarkArticle(article)
                    }
                )
            }

            composable(Screen.Search.route) {
                val viewModel: NewsViewModel = hiltViewModel()
                val context = LocalContext.current
                SearchNewsScreen(
                    viewModel,
                    onArticleClick = { article ->
                        OpenArticleInBrowser.launch(context, article.url)
                    },
                    onBookmarkClick = { article ->
                        viewModel.toggleBookmarkArticle(article)
                    }
                )
            }
            /* // WebView Screen composable (WebView is not recommended in android app)
            composable(
                route = "${Screen.DetailNews.route}/{articleUrl}",
                arguments = listOf(
                    navArgument("articleUrl") {
                        type = NavType.StringType
                    }
                )
            ) {
                val encodedUrl = it.arguments?.getString("articleUrl") ?: ""
                val articleUrl = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
                WebViewScreen(
                    articleUrl = articleUrl,
                    onBackPressed = {
                        navController.navigateUp()
                    }
                )
            }
            */
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun NewsAppMainPreview() {
    NewsAppTheme(darkTheme = false) {
        NewsAppMain()
    }
}


