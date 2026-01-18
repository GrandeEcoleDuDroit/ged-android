package com.upsaclay.gedoise.presentation.navigation

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDestination
import com.upsaclay.authentication.AuthenticationBaseRoute
import com.upsaclay.authentication.AuthenticationRoute
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.common.domain.repository.RouteRepository
import com.upsaclay.common.domain.usecase.NavigationRequestUseCase
import com.upsaclay.message.domain.usecase.GetUnreadConversationsCountUseCase
import com.upsaclay.message.presentation.chat.ChatRoute
import com.upsaclay.news.presentation.NewsBaseRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NavigationViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val routeRepository: RouteRepository,
    private val getUnreadConversationsCountUseCase: GetUnreadConversationsCountUseCase,
    navigationRequestUseCase: NavigationRequestUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(NavigationState())
    val uiState: StateFlow<NavigationState> = _uiState
    val routeToNavigate: Flow<List<Route>> = navigationRequestUseCase.routesToNavigate.map {
        if (authenticationRepository.isAuthenticated) {
            it
        } else {
            listOf(AuthenticationRoute)
        }
    }.onEach {
        navigationRequestUseCase.resetRoute()
    }

    init {
        updateStartDestination()
        updateMessageBadges()
    }

    fun setCurrentRoute(destination: NavDestination, arguments: Bundle?) {
        val route = resolveRoute(destination, arguments)
        viewModelScope.launch {
            routeRepository.setCurrentRoute(route)
        }
    }

    private fun resolveRoute(destination: NavDestination, arguments: Bundle?): Route? {
        val routeName = destination.route?.split('.')?.last() ?: return null
        return when {
            routeName.startsWith(ChatRoute.NAME) ->
                arguments?.getString(ChatRoute.CONVERSATION_JSON_ARGUMENT)?.let { ChatRoute(conversationJson = it) }

            else -> null
        }
    }

    private fun updateStartDestination() {
        viewModelScope.launch {
            authenticationRepository.authenticated.map {
                if (it) NewsBaseRoute else AuthenticationBaseRoute
            }.collect { route ->
                _uiState.update {
                    it.copy(startDestination = route)
                }
            }
        }
    }

    private fun updateMessageBadges() {
        viewModelScope.launch {
            getUnreadConversationsCountUseCase().collect { number ->
                _uiState.update {
                    it.copy(
                        topLevelDestinations = it.topLevelDestinations.map { destination ->
                            (destination as? TopLevelDestination.Message)?.copy(badges = number) ?: destination
                        }
                    )
                }
            }
        }
    }

    data class NavigationState(
        val topLevelDestinations: List<TopLevelDestination> = listOf(
            TopLevelDestination.Home,
            TopLevelDestination.Message(),
            TopLevelDestination.Mission,
            TopLevelDestination.Profile
        ),
        val startDestination: Route = SplashRoute
    )
}