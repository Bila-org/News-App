package com.example.newsapp.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsapp.presentation.HeadlineScreen.HeadlinesNewsScreen
import com.example.newsapp.presentation.HeadlineScreen.HeadlinesViewModel
import com.example.newsapp.presentation.SavedNewsScreen.SavedNewsScreen
import com.example.newsapp.presentation.SavedNewsScreen.SavedViewModel
import com.example.newsapp.presentation.SearchNewsScreen.SearchNewsScreen
import com.example.newsapp.presentation.SearchNewsScreen.SearchViewModel
import com.example.newsapp.presentation.common.components.AppBottomNavigationBar
import com.example.newsapp.presentation.common.components.AppTopBar
import com.example.newsapp.presentation.common.components.Screen
import com.example.newsapp.presentation.common.components.WebViewScreen
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsAppMain(
    onNotificationEnableClick: () -> Unit,
    isNotificationEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    LaunchedEffect(isNotificationEnabled) {
        if (isNotificationEnabled) {
            scope.launch {
                snackbarHostState
                    .showSnackbar(
                        message = "Notifications are enabled",
                        duration = SnackbarDuration.Short
                    )
            }
        }
    }


    Scaffold(
        topBar = {
            AppTopBar(
                onClick = {
                    onNotificationEnableClick()
                },
                isNotificationEnabled = isNotificationEnabled
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
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Headlines.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Headlines.route) {
                val viewModel: HeadlinesViewModel = hiltViewModel()
                HeadlinesNewsScreen(
                    uiState = viewModel.uiState.value,
                    onArticleClick = { article ->
                        val encodedUrl =
                            URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
                        navController.navigate(
                            "${Screen.DetailNews.route}/${encodedUrl}"
                        )
                    },
                    onBookmarkClick = { article ->
                        viewModel.addToBookmarkArticle(article)
                        scope.launch {
                            snackbarHostState
                                .showSnackbar(
                                    message = "Added to bookmark news",
                                    duration = SnackbarDuration.Short
                                )
                        }
                    }
                )
            }

            composable(Screen.Saved.route) {
                val viewModel: SavedViewModel = hiltViewModel()
                SavedNewsScreen(
                    uiState = viewModel.uiState.value,
                    onArticleClick = { article ->
                        val encodedUrl =
                            URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
                        navController.navigate(
                            "${Screen.DetailNews.route}/${encodedUrl}"
                        )
                    },
                    onBookmarkClick = { article ->
                        viewModel.deleteArticle(article)
                        scope.launch {
                            val result = snackbarHostState
                                .showSnackbar(
                                    message = "Bookmark news is deleted",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Short
                                )
                            when (result) {
                                SnackbarResult.ActionPerformed -> {
                                    viewModel.addToBookmarkArticle(article)
                                }

                                SnackbarResult.Dismissed -> {
                                }
                            }
                        }
                    },
                    swipeToDelete = { article ->
                        viewModel.deleteArticle(article)
                        scope.launch {
                            val result = snackbarHostState
                                .showSnackbar(
                                    message = "Bookmark news is deleted",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Short
                                )
                            when (result) {
                                SnackbarResult.ActionPerformed -> {
                                    viewModel.addToBookmarkArticle(article)
                                }

                                SnackbarResult.Dismissed -> {
                                }
                            }
                        }
                    }
                )
            }

            composable(Screen.Search.route) {
                val viewModel: SearchViewModel = hiltViewModel()
                SearchNewsScreen(
                    uiState = viewModel.uiState.value,
                    onArticleClick = { article ->
                        val encodedUrl =
                            URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
                        navController.navigate(
                            "${Screen.DetailNews.route}/${encodedUrl}"
                        )
                    },
                    onBookmarkClick = { article ->
                        viewModel.addToBookmarkArticle(article)
                        scope.launch {
                            snackbarHostState
                                .showSnackbar(
                                    message = "Added to bookmark news",
                                    duration = SnackbarDuration.Short
                                )
                        }
                    },
                    onSearchQuery = { query ->
                        viewModel.searchForNews(query)
                    }
                )
            }


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
        }
    }
}