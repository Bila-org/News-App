package com.example.newsapp.presentation.common.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FindInPage
import androidx.compose.ui.graphics.vector.ImageVector


data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

val navigationItems = listOf(
    NavigationItem(
        title = "Headlines",
        selectedIcon = Icons.AutoMirrored.Filled.Article,
        unselectedIcon = Icons.AutoMirrored.Outlined.Article,
        route = "headlines"
    ),
    NavigationItem(
        title = "Saved",
        selectedIcon = Icons.Filled.Bookmark,
        unselectedIcon = Icons.Outlined.BookmarkBorder,
        route = "saved"
    ),
    NavigationItem(
        title = "Search",
        selectedIcon = Icons.Filled.FindInPage,
        unselectedIcon = Icons.Outlined.FindInPage,
        route = "search"
    )
)

sealed class Screen(val route: String) {
    object Headlines : Screen("headlines")
    object Saved : Screen("saved")
    object Search : Screen("search")
    object DetailNews : Screen("detailNews")
}
