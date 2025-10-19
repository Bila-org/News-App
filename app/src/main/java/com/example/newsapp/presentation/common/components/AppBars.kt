package com.example.newsapp.presentation.common.components


import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.R
import com.example.newsapp.ui.theme.NewsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    onClick: () -> Unit,
    isNotificationEnabled: Boolean = false
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
        actions = {
            IconButton(
                onClick = {
                    onClick()
                },
                enabled = !isNotificationEnabled
            ) {
                Icon(
                    imageVector = if (isNotificationEnabled)
                        Icons.Default.Notifications
                    else
                        Icons.Default.NotificationsNone,
                    contentDescription = "Enable notifications",
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}


@Composable
fun AppBottomNavigationBar(
    onClick: (NavigationItem) -> Unit = {},
    navController: NavController,
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination?.route

    NavigationBar(
        modifier = Modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface
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


@Preview(
    showBackground = true,
)
@Composable
fun AppTopBarPreview() {
    NewsAppTheme(darkTheme = false) {
        AppTopBar(
            onClick = { },
            isNotificationEnabled = false
        )
    }
}


@Preview(
    showBackground = true,
)
@Composable
fun AppBottomNavigationBarPreview() {
    NewsAppTheme(darkTheme = false) {
        val navController = rememberNavController()
    //    navController.navigate(Screen.Headlines.route)
        AppBottomNavigationBar(
            onClick = { },
            navController = navController
        )
    }
}