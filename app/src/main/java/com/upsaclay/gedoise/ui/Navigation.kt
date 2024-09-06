package com.upsaclay.gedoise.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.upsaclay.authentication.ui.AuthenticationScreen
import com.upsaclay.authentication.ui.registration.FirstRegistrationScreen
import com.upsaclay.authentication.ui.registration.RegistrationViewModel
import com.upsaclay.authentication.ui.registration.SecondRegistrationScreen
import com.upsaclay.authentication.ui.registration.ThirdRegistrationScreen
import com.upsaclay.common.domain.model.Screen
import com.upsaclay.common.domain.model.User
import com.upsaclay.gedoise.data.BottomNavigationItem
import com.upsaclay.gedoise.ui.profile.ProfileScreen
import com.upsaclay.gedoise.ui.profile.account.AccountScreen
import com.upsaclay.news.ui.CreateAnnouncementScreen
import com.upsaclay.news.ui.NewsScreen
import com.upsaclay.news.ui.NewsViewModel
import com.upsaclay.news.ui.ReadAnnouncementScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun Navigation(mainViewModel: MainViewModel = koinViewModel()) {
    val navController = rememberNavController()
    val user = mainViewModel.user.collectAsState(null).value
    val isAuthenticated by mainViewModel.isAuthenticated.collectAsState(false)
    val sharedRegistrationViewModel: RegistrationViewModel = koinViewModel()
    val sharedNewsViewModel: NewsViewModel = koinViewModel()
    val startDestination = if (isAuthenticated) {
        Screen.NEWS.route
    } else {
        Screen.NEWS.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.AUTHENTICATION.route) {
            AuthenticationScreen(navController = navController)
        }

        composable(Screen.FIRST_REGISTRATION_SCREEN.route) {
            FirstRegistrationScreen(
                navController = navController,
                registrationViewModel = sharedRegistrationViewModel
            )
        }

        composable(Screen.SECOND_REGISTRATION_SCREEN.route) {
            SecondRegistrationScreen(
                navController = navController,
                registrationViewModel = sharedRegistrationViewModel
            )
        }

        composable(Screen.THIRD_REGISTRATION_SCREEN.route) {
            ThirdRegistrationScreen(
                navController = navController,
                registrationViewModel = sharedRegistrationViewModel
            )
        }

        composable(Screen.NEWS.route) {
            user?.let {
                MainNavigationBars(
                    navController = navController,
                    bottomNavigationItems = mainViewModel.bottomNavigationItem.values.toList(),
                    user = user
                ) {
                    NewsScreen(
                        navController = navController,
                        newsViewModel = sharedNewsViewModel
                    )
                }
            }
        }

        composable(Screen.READ_ANNOUNCEMENT.route) {
            Scaffold(
                topBar = {
                    SmallTopBarBack(
                        navController = navController,
                        title = stringResource(id = com.upsaclay.news.R.string.announcement)
                    )
                }
            ) { contentPadding ->
                ReadAnnouncementScreen(
                    modifier = Modifier.padding(top = contentPadding.calculateTopPadding()),
                    navController = navController,
                    newsViewModel = sharedNewsViewModel
                )
            }
        }

        composable(Screen.CREATE_ANNOUNCEMENT.route) {
            CreateAnnouncementScreen(
                navController = navController,
                newsViewModel = sharedNewsViewModel
            )
        }

        composable(Screen.MESSAGES.route) {
            user?.let {
                MainNavigationBars(
                    navController = navController,
                    bottomNavigationItems = mainViewModel.bottomNavigationItem.values.toList(),
                    user = user
                ) {
                    Text(text = "Message")
                }
            }
        }

        composable(Screen.CALENDAR.route) {
            user?.let {
                MainNavigationBars(
                    navController = navController,
                    bottomNavigationItems = mainViewModel.bottomNavigationItem.values.toList(),
                    user = user
                ) {
                    Text(text = "Calendar")
                }
            }
        }

        composable(Screen.FORUM.route) {
            user?.let {
                MainNavigationBars(
                    navController = navController,
                    bottomNavigationItems = mainViewModel.bottomNavigationItem.values.toList(),
                    user = user
                ) {
                    Text(text = "Forum")
                }
            }
        }

        composable(Screen.PROFILE.route) {
            ProfileScreen(navController = navController)
        }

        composable(Screen.ACCOUNT.route) {
            AccountScreen(navController = navController)
        }
    }

}

@Composable
private fun MainNavigationBars(
    navController: NavController,
    bottomNavigationItems: List<BottomNavigationItem>,
    user: User,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = { MainTopBar(navController = navController, user = user) },
        bottomBar = {
            MainBottomBar(
                navController = navController,
                bottomNavigationItems = bottomNavigationItems
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
