package com.upsaclay.gedoise.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.upsaclay.authentication.ui.AuthenticationScreen
import com.upsaclay.core.data.Screen
import com.upsaclay.core.ui.theme.GedoiseTheme
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
        Column(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp
            )
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainTopBar(
    navController: NavController
){
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.school_name),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { navController.navigate(Screen.HOME.route) }
            ) {
                Image(
                    painter = painterResource(id = com.upsaclay.core.R.drawable.ged_logo),
                    contentDescription = stringResource(id = com.upsaclay.core.R.string.ged_logo_description),
                    contentScale = ContentScale.Fit
                )
            }
        }
    )
}

@Composable
private fun MainBottomBar(
    navController: NavController,
    navigationItems: List<NavigationItem>
){
    val currentRoute = remember {
        navController.currentDestination?.route
    }

    NavigationBar{
        navigationItems.forEachIndexed { index, navigationItem ->
            NavigationBarItem(
                selected = navigationItem.screen.route == currentRoute,
                onClick = {
                    if(navigationItem.screen.route != currentRoute)
                        navController.navigate(navigationItem.screen.route)
                },
                icon = {
                    BadgedBox(badge = {
                        if (navigationItem.badges > 0) {
                            Badge {
                                Text(text = navigationItem.badges.toString())
                            }
                        } else if (navigationItem.hasNews) {
                            Badge()
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = navigationItem.icon),
                            contentDescription = stringResource(id = navigationItem.iconDescription)
                        )
                    }
                },
                label = {
                    Text(text = stringResource(id = navigationItem.label))
                }
            )
        }
    }
}

@Composable
private fun RowScope.AddItem(
    navController: NavController,
    navigationItem: NavigationItem
){
    val currentRoute = remember {
        navController.currentDestination?.route
    }

    NavigationBarItem(
        modifier = Modifier.weight(1f),
        label = { Text(text = stringResource(id = navigationItem.label)) },
        icon = {
            Icon(
                painter = painterResource(id = navigationItem.icon),
                contentDescription = stringResource(id = navigationItem.iconDescription)
            )
        },
        selected = navigationItem.screen.route == currentRoute,
        alwaysShowLabel = true,
        onClick = {
            if(navigationItem.screen.route != currentRoute)
                navController.navigate(navigationItem.screen.route)
        },
    )
}


@Preview
@Composable
private fun NavigationBarPreview(){
    val messageWithNotif = NavigationItem.Message()
    messageWithNotif.badges = 5

    val calendarWithNews = NavigationItem.Calendar()
    calendarWithNews.hasNews = true

    val itemList = listOf(
        NavigationItem.Home(),
        messageWithNotif,
        calendarWithNews,
        NavigationItem.Forum(),
    )

    GedoiseTheme {
        Scaffold(
            topBar = { MainTopBar(navController = NavController(LocalContext.current)) },
            bottomBar = { MainBottomBar(
                navController = NavController(LocalContext.current),
                itemList
            ) }
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
            )
            {
                Text(
                    text = "Navigation Bar Preview",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }
    }
}