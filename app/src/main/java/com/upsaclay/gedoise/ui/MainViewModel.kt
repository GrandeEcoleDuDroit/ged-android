package com.upsaclay.gedoise.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.domain.usecase.IsAuthenticatedUseCase
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.usecase.GetCurrentUser
import com.upsaclay.gedoise.data.NavigationItem
import com.upsaclay.gedoise.data.NavigationItemType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase,
    private val getCurrentUser: GetCurrentUser
): ViewModel() {
    private val _navigationItems = MutableStateFlow(
        mapOf(
            NavigationItemType.HOME to NavigationItem.Home(),
            NavigationItemType.MESSAGE to NavigationItem.Message(),
            NavigationItemType.CALENDAR to NavigationItem.Calendar(),
            NavigationItemType.FORUM to NavigationItem.Forum()
        )
    )
    val navigationItem: StateFlow<Map<NavigationItemType, NavigationItem>> =
        _navigationItems.asStateFlow()

    private val _isAuthenticated: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isAuthenticated: StateFlow<Boolean?> = _isAuthenticated.asStateFlow()
    lateinit var currentUser: User

    init {
        viewModelScope.launch {
            _isAuthenticated.value = isAuthenticatedUseCase()
        }

        viewModelScope.launch {
            currentUser = getCurrentUser()
        }
    }
}