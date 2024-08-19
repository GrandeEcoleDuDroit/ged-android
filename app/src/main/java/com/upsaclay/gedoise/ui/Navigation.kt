package com.upsaclay.gedoise.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.upsaclay.authentication.ui.AuthenticationScreen
import com.upsaclay.authentication.ui.registration.FirstRegistrationScreen
import com.upsaclay.authentication.ui.registration.RegistrationViewModel
import com.upsaclay.authentication.ui.registration.SecondRegistrationScreen
import com.upsaclay.authentication.ui.registration.ThirdRegistrationScreen
import com.upsaclay.common.data.model.Screen
import com.upsaclay.common.domain.model.User
import com.upsaclay.gedoise.data.BottomNavigationItem
import com.upsaclay.gedoise.ui.profile.ProfileScreen
import com.upsaclay.gedoise.ui.profile.account.AccountInfoScreen
import com.upsaclay.news.ui.NewsScreen
import com.upsaclay.news.ui.NewsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun Navigation(
    mainViewModel: MainViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val user = mainViewModel.user.collectAsState(initial = null).value
    var startDestination: String? by remember { mutableStateOf(null) }

    val sharedRegistrationViewModel: RegistrationViewModel = koinViewModel()
    val sharedNewsViewModel: NewsViewModel = koinViewModel()

    LaunchedEffect(mainViewModel.isAuthenticated) {
        mainViewModel.isAuthenticated.collect {
            startDestination = if(it) {
                Screen.NEWS.route
            }
            else {
                Screen.NEWS.route
            }
        }
    }

    startDestination?.let {
        NavHost(
            navController = navController,
            startDestination = it
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

            user?.let { user ->
                composable(Screen.NEWS.route) {
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

                composable(Screen.MESSAGES.route) {
                    MainNavigationBars(
                        navController = navController,
                        bottomNavigationItems = mainViewModel.bottomNavigationItem.values.toList(),
                        user = user
                    ) {
                        Text(text = "Message")
                    }
                }

                composable(Screen.CALENDAR.route) {
                    MainNavigationBars(
                        navController = navController,
                        bottomNavigationItems = mainViewModel.bottomNavigationItem.values.toList(),
                        user = user
                    ) {
                        Text(text = "Calendar")
                    }
                }

                composable(Screen.FORUM.route) {
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

            composable(Screen.ACCOUNT_INFOS.route) {
                AccountInfoScreen(navController = navController)
            }
        }
    }
}

@Composable
private fun MainNavigationBars(
    navController: NavController,
    bottomNavigationItems: List<BottomNavigationItem>,
    user: User,
    content: @Composable () -> Unit
){
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
