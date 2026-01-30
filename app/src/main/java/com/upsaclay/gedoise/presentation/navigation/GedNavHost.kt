package com.upsaclay.gedoise.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.upsaclay.authentication.authenticationSection
import com.upsaclay.authentication.presentation.forgottenpassword.forgottenPasswordScreen
import com.upsaclay.authentication.presentation.forgottenpassword.navigateToForgottenPasswordScreen
import com.upsaclay.authentication.presentation.registration.firstregistration.firstRegistrationScreen
import com.upsaclay.authentication.presentation.registration.firstregistration.navigateToFirstRegistration
import com.upsaclay.authentication.presentation.registration.secondregistration.navigateToSecondRegistration
import com.upsaclay.authentication.presentation.registration.secondregistration.secondRegistrationScreen
import com.upsaclay.authentication.presentation.registration.thirdregistration.navigateToThirdRegistration
import com.upsaclay.authentication.presentation.registration.thirdregistration.thirdRegistrationScreen
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.common.presentation.user.navigateToUser
import com.upsaclay.common.presentation.user.userScreen
import com.upsaclay.gedoise.presentation.components.MainBottomBar
import com.upsaclay.gedoise.presentation.profile.account.accountScreen
import com.upsaclay.gedoise.presentation.profile.account.deleteaccount.deleteAccountScreen
import com.upsaclay.gedoise.presentation.profile.account.deleteaccount.navigateToDeleteAccount
import com.upsaclay.gedoise.presentation.profile.account.navigateToAccount
import com.upsaclay.gedoise.presentation.profile.accountinformation.accountInformationScreen
import com.upsaclay.gedoise.presentation.profile.accountinformation.navigateToAccountInformation
import com.upsaclay.gedoise.presentation.profile.blockedusers.blockedUsersScreen
import com.upsaclay.gedoise.presentation.profile.blockedusers.navigateToBlockedUsers
import com.upsaclay.gedoise.presentation.profile.navigateToProfile
import com.upsaclay.gedoise.presentation.profile.privacy.navigateToPrivacy
import com.upsaclay.gedoise.presentation.profile.privacy.privacyScreen
import com.upsaclay.gedoise.presentation.profile.profileSection
import com.upsaclay.message.presentation.chat.ChatRoute
import com.upsaclay.message.presentation.chat.chatScreen
import com.upsaclay.message.presentation.chat.navigateToChat
import com.upsaclay.message.presentation.conversation.ConversationBaseRoute
import com.upsaclay.message.presentation.conversation.ConversationRoute
import com.upsaclay.message.presentation.conversation.conversationSection
import com.upsaclay.message.presentation.conversation.createconversation.CreateConversationRoute
import com.upsaclay.message.presentation.conversation.createconversation.createConversationScreen
import com.upsaclay.message.presentation.conversation.createconversation.navigateToCreateConversation
import com.upsaclay.message.presentation.conversation.navigateToConversation
import com.upsaclay.mission.presentation.createmission.createMissionScreen
import com.upsaclay.mission.presentation.createmission.navigateToCreateMission
import com.upsaclay.mission.presentation.editmission.editMissionScreen
import com.upsaclay.mission.presentation.editmission.navigateToEditMission
import com.upsaclay.mission.presentation.missionSection
import com.upsaclay.mission.presentation.missiondetails.allusers.allUsersScreen
import com.upsaclay.mission.presentation.missiondetails.allusers.navigateToAllUsers
import com.upsaclay.mission.presentation.missiondetails.missionDetailsScreen
import com.upsaclay.mission.presentation.missiondetails.navigateToMissionDetails
import com.upsaclay.mission.presentation.navigateToMission
import com.upsaclay.news.presentation.NewsRoute
import com.upsaclay.news.presentation.announcement.allannouncements.allAnnouncementsScreen
import com.upsaclay.news.presentation.announcement.allannouncements.navigateToAllAnnouncements
import com.upsaclay.news.presentation.announcement.createannouncement.createAnnouncementScreen
import com.upsaclay.news.presentation.announcement.createannouncement.navigateToCreateAnnouncement
import com.upsaclay.news.presentation.announcement.editannouncement.editAnnouncementScreen
import com.upsaclay.news.presentation.announcement.editannouncement.navigateToEditAnnouncement
import com.upsaclay.news.presentation.announcement.readannouncement.navigateToReadAnnouncement
import com.upsaclay.news.presentation.announcement.readannouncement.readAnnouncementScreen
import com.upsaclay.news.presentation.navigateToNews
import com.upsaclay.news.presentation.newsSection
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable data object SplashRoute: Route

@Composable
fun GedNavHost(
    navigationViewModel: NavigationViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val uiState by navigationViewModel.uiState.collectAsState()
    val currentEntry = navController.currentBackStackEntryAsState()
    val navOptions = NavOptions.Builder()
        .setLaunchSingleTop(true)
        .setRestoreState(true)
        .setPopUpTo(
            route = NewsRoute,
            inclusive = false,
            saveState = true
        )
        .build()

    fun NavController.navigateToTopLevelDestination(destination: TopLevelDestinationRoute) {
        when (destination) {
            TopLevelDestinationRoute.HOME -> {
                popBackStack()
                navigateToNews(navOptions = navOptions)
            }

            TopLevelDestinationRoute.MESSAGE -> navigateToConversation(navOptions = navOptions)

            TopLevelDestinationRoute.MISSION -> navigateToMission(navOptions = navOptions)

            TopLevelDestinationRoute.PROFILE -> navigateToProfile(navOptions = navOptions)
        }
    }

    val bottomBar: @Composable () -> Unit = {
        MainBottomBar(
            onTopLevelDestinationClick = navController::navigateToTopLevelDestination,
            currentRoute = currentEntry.value?.destination,
            topLevelDestinations = uiState.topLevelDestinations
        )
    }

    LaunchedEffect(Unit) {
        navigationViewModel.routeToNavigate.collect { routes ->
            routes.forEach {
                when (it) {
                    is ConversationRoute -> navController.navigateToConversation()
                    is ChatRoute -> navController.navigateToChat(it.conversationJson)
                    else -> Unit
                }
            }
        }
    }

    navController.addOnDestinationChangedListener { _, destination, arguments ->
        navigationViewModel.setCurrentRoute(destination, arguments)
    }

    NavHost(
        navController = navController,
        startDestination = uiState.startDestination
    ) {
        composable<SplashRoute> {}

        authenticationSection(
            onRegistrationClick = navController::navigateToFirstRegistration,
            onLoginClick = navController::navigateToNews,
            onForgottenPasswordClick = navController::navigateToForgottenPasswordScreen
        ) {
            firstRegistrationScreen(
                onBackClick = navController::popBackStack,
                onNextClick = navController::navigateToSecondRegistration
            )

            secondRegistrationScreen(
                onBackClick = navController::popBackStack,
                onNextClick = navController::navigateToThirdRegistration
            )

            thirdRegistrationScreen(
                onBackClick = navController::popBackStack,
                onRegistrationClick = navController::navigateToNews
            )

            forgottenPasswordScreen(
             onBackClick = navController::popBackStack
            )
        }

        newsSection(
            onAnnouncementClick = navController::navigateToReadAnnouncement,
            onCreateAnnouncementClick = navController::navigateToCreateAnnouncement,
            onEditAnnouncementClick = navController::navigateToEditAnnouncement,
            onSeeAllAnnouncementsClick = navController::navigateToAllAnnouncements,
            bottomBar = bottomBar
        ) {
            createAnnouncementScreen(
                onBackClick = navController::popBackStack
            )

            readAnnouncementScreen(
                onBackClick = navController::popBackStack,
                onEditAnnouncementClick = navController::navigateToEditAnnouncement,
                onAuthorClick = navController::navigateToUser
            )

            editAnnouncementScreen(
                onBackClick = navController::popBackStack
            )

            allAnnouncementsScreen(
                onBackClick = navController::popBackStack,
                onAnnouncementClick = navController::navigateToReadAnnouncement,
                onEditAnnouncementClick = navController::navigateToEditAnnouncement,
                onAuthorClick = navController::navigateToUser
            )
        }

        conversationSection(
            onConversationClick = navController::navigateToChat,
            onCreateConversation = navController::navigateToCreateConversation,
            bottomBar = bottomBar
        ) {
            createConversationScreen(
                onBackClick = navController::popBackStack,
                onCreateConversationClick = {
                    navController.navigateToChat(it) {
                        popUpTo(CreateConversationRoute) {
                            inclusive = true
                        }
                    }
                }
            )

            chatScreen(
                onBackClick = {
                    navController.navigateToConversation {
                        popUpTo(ConversationBaseRoute)
                    }
                },
                onInterlocutorClick = navController::navigateToUser
            )
        }

        profileSection(
            onAccountInformationClick = navController::navigateToAccountInformation,
            onAccountClick = navController::navigateToAccount,
            onPrivacyClick = navController::navigateToPrivacy,
            bottomBar = bottomBar
        ) {
            accountInformationScreen(onBackClick = navController::popBackStack)

            accountScreen(
                onBackClick = navController::popBackStack,
                onDeleteAccountClick = navController::navigateToDeleteAccount
            )

            deleteAccountScreen(onBackClick = navController::popBackStack)

            privacyScreen(
                onBackClick = navController::popBackStack,
                onBlockedUsersClick = navController::navigateToBlockedUsers
            )

            blockedUsersScreen(
                onBackClick = navController::popBackStack,
                onAccountClick = navController::navigateToUser
            )
        }

        userScreen(onBackClick = navController::popBackStack)

        missionSection(
            onMissionClick = navController::navigateToMissionDetails,
            onCreateMissionClick = navController::navigateToCreateMission,
            onEditMissionClick = navController::navigateToEditMission,
            bottomBar = bottomBar
        ) {
            createMissionScreen(
                onBackClick = navController::popBackStack
            )

            missionDetailsScreen(
                onBackClick = navController::popBackStack,
                onManagerClick = navController::navigateToUser,
                onParticipantClick = navController::navigateToUser,
                onEditMissionClick = navController::navigateToEditMission,
                onSeeAllUsersClick = navController::navigateToAllUsers
            )

            editMissionScreen(
                onBackClick = navController::popBackStack
            )

            allUsersScreen(
                onBackClick = navController::popBackStack,
                onUserClick = navController::navigateToUser
            )
        }
    }
}