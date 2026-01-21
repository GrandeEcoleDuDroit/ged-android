package com.upsaclay.gedoise.viewmodel

import com.upsaclay.authentication.AuthenticationBaseRoute
import com.upsaclay.authentication.AuthenticationRoute
import com.upsaclay.authentication.domain.entity.AuthenticationState
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.repository.RouteRepository
import com.upsaclay.common.domain.usecase.NavigationRequestUseCase
import com.upsaclay.gedoise.presentation.navigation.NavigationViewModel
import com.upsaclay.gedoise.presentation.navigation.TopLevelDestination
import com.upsaclay.message.domain.conversationFixture
import com.upsaclay.message.domain.converter.ConversationJsonParser
import com.upsaclay.message.domain.usecase.GetUnreadConversationsCountUseCase
import com.upsaclay.message.presentation.chat.ChatRoute
import com.upsaclay.news.presentation.NewsBaseRoute
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class NavigationViewModelTest {
    private val routeRepository: RouteRepository = mockk()
    private val getUnreadConversationsCountUseCase: GetUnreadConversationsCountUseCase = mockk()
    private val authenticationRepository: AuthenticationRepository = mockk()
    private val navigationRequestUseCase: NavigationRequestUseCase = mockk()

    private lateinit var viewModel: NavigationViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val userId = "userId1234"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { navigationRequestUseCase.routesToNavigate } returns flowOf()
        every { navigationRequestUseCase.resetRoute() } returns Unit
        every { authenticationRepository.authenticationState } returns flowOf(AuthenticationState.Authenticated(userId))
        every { routeRepository.setCurrentRoute(any()) } returns Unit
        every { getUnreadConversationsCountUseCase() } returns flowOf(0)
    }

    @Test
    fun startDestination_should_be_NewsRoute_when_authenticated() = runTest {
        // When
        viewModel = NavigationViewModel(
            getUnreadConversationsCountUseCase = getUnreadConversationsCountUseCase,
            routeRepository = routeRepository,
            authenticationRepository = authenticationRepository,
            navigationRequestUseCase = navigationRequestUseCase
        )

        // Then
        val result = viewModel.uiState.value.startDestination

        assertEquals(NewsBaseRoute, result)
    }

    @Test
    fun startDestination_should_be_AuthenticationRoute_when_unauthenticated() = runTest {
        // Given
        every { authenticationRepository.authenticationState } returns flowOf(AuthenticationState.Unauthenticated)

        // When
        viewModel = NavigationViewModel(
            getUnreadConversationsCountUseCase = getUnreadConversationsCountUseCase,
            routeRepository = routeRepository,
            authenticationRepository = authenticationRepository,
            navigationRequestUseCase = navigationRequestUseCase
        )

        // Then
        val result = viewModel.uiState.value.startDestination

        assertEquals(AuthenticationBaseRoute, result)
    }

    @Test
    fun routeToNavigate_should_be_received_one_route_when_authenticated() = runTest {
        // Given
        val route = listOf(ChatRoute(ConversationJsonParser.toJson(conversationFixture)))
        every { navigationRequestUseCase.routesToNavigate } returns flowOf(route)

        // When
        viewModel = NavigationViewModel(
            getUnreadConversationsCountUseCase = getUnreadConversationsCountUseCase,
            routeRepository = routeRepository,
            authenticationRepository = authenticationRepository,
            navigationRequestUseCase = navigationRequestUseCase
        )

        // Then
        val result = viewModel.routeToNavigate.first()

        assert(result == route)
    }

    @Test
    fun routeToNavigate_should_be_AuthenticationRoute_route_when_unauthenticated() = runTest {
        // Given
        val route = listOf(ChatRoute(ConversationJsonParser.toJson(conversationFixture)))
        every { authenticationRepository.authenticationState } returns flowOf(AuthenticationState.Unauthenticated)
        every { navigationRequestUseCase.routesToNavigate } returns flowOf(route)

        // When
        viewModel = NavigationViewModel(
            getUnreadConversationsCountUseCase = getUnreadConversationsCountUseCase,
            routeRepository = routeRepository,
            authenticationRepository = authenticationRepository,
            navigationRequestUseCase = navigationRequestUseCase
        )

        // Then
        val result = viewModel.routeToNavigate.first()

        assertEquals(listOf(AuthenticationRoute), result)
    }

    @Test
    fun routeToNavigate_should_be_reset_after_being_received() = runTest {
        // Given
        val route = listOf(ChatRoute(ConversationJsonParser.toJson(conversationFixture)))
        every { navigationRequestUseCase.routesToNavigate } returns flowOf(route)

        // When
        viewModel = NavigationViewModel(
            getUnreadConversationsCountUseCase = getUnreadConversationsCountUseCase,
            routeRepository = routeRepository,
            authenticationRepository = authenticationRepository,
            navigationRequestUseCase = navigationRequestUseCase
        )

        viewModel.routeToNavigate.first()

        verify { navigationRequestUseCase.resetRoute() }
    }

    @Test
    fun updateMessageBadges_should_be_equals_to_unread_conversations_count() {
        // Given
        every { getUnreadConversationsCountUseCase() } returns flowOf(2)

        // When
        viewModel = NavigationViewModel(
            getUnreadConversationsCountUseCase = getUnreadConversationsCountUseCase,
            routeRepository = routeRepository,
            authenticationRepository = authenticationRepository,
            navigationRequestUseCase = navigationRequestUseCase
        )

        // Then
        val result = viewModel.uiState.value.topLevelDestinations
        val topLevelDestination = result.find { it is TopLevelDestination.Message } as TopLevelDestination.Message

        assertEquals(2, topLevelDestination.badges)
    }
}