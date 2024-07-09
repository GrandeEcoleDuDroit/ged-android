package com.upsaclay.gedoise.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.upsaclay.authentication.ui.AuthenticationScreen
import com.upsaclay.core.data.Screen
import com.upsaclay.gedoise.R
import com.upsaclay.gedoise.data.NavigationItem
import com.upsaclay.news.ui.NewsScreen
import org.koin.androidx.compose.koinViewModel


@Composable
fun Navigation(
    mainViewModel: MainViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val navigationItems by mainViewModel.navigationItem.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.AUTHENTICATION.route
    ) {
        composable(Screen.AUTHENTICATION.route) {
            AuthenticationScreen(navController = navController)
        }

        composable(Screen.HOME.route) {
            MainNavigationBars(
                navController = navController,
                navigationItems.values.toList()
            ) {
                NewsScreen()
            }
        }

        composable(Screen.MESSAGE.route) {
            MainNavigationBars(
                navController = navController,
                navigationItems.values.toList()
            ) {
                Text(text = "Message")
            }
        }

        composable(Screen.CALENDAR.route) {
            MainNavigationBars(
                navController = navController,
                navigationItems.values.toList()
            ) {
                Text(text = "Calendar")
            }
        }

        composable(Screen.FORUM.route) {
            MainNavigationBars(
                navController = navController,
                navigationItems.values.toList()
            ) {
                Text(text = "Forum")
            }
        }
    }
}

@Composable
private fun MainNavigationBars(
    navController: NavController,
    navigationItems: List<NavigationItem>,
    content: @Composable () -> Unit
){
    Scaffold(
        topBar = {
            MainTopBar(navController = navController)
        },
        bottomBar = {
            MainBottomBar(
                navController = navController,
                navigationItems = navigationItems
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding()
                )
        ) {
            content()
        }
    }
}
