package com.example.newsapp.presentation.ui.common

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.newsapp.presentation.NavigationItem
import com.example.newsapp.presentation.navigationItems


@Composable
fun AppBottomNavigationBar(
    onClick: (NavigationItem) -> Unit = {},
    navController: NavController,
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination?.route

    NavigationBar(
        modifier = Modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer, // Background color
        contentColor = MaterialTheme.colorScheme.onSurface // Default content color)
    ) {
        navigationItems.forEach { screen ->
            NavigationBarItem(
                selected = currentDestination == screen.route,
                onClick = {
                    onClick(screen)
                },
                icon = {
                    Icon(
                        imageVector = if (currentDestination == screen.route) {
                            screen.selectedIcon
                        } else {
                            screen.unselectedIcon
                        },
                        contentDescription = screen.title
                    )
                },
                modifier = Modifier,
                label = {
                    if (currentDestination == screen.route) {
                        Text(
                            text = screen.title,
                            fontWeight = FontWeight.Bold
                        )

                    } else {
                        Text(
                            text = screen.title,
                            fontWeight = FontWeight.Normal
                        )
                    }
                },
                colors =
                    NavigationBarItemColors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,  // primary
                        selectedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    )
            )
        }
    }
}
