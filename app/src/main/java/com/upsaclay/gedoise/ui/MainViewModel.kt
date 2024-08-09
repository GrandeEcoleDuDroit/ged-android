package com.upsaclay.gedoise.ui

import androidx.lifecycle.ViewModel
import com.upsaclay.authentication.domain.usecase.IsAuthenticatedFlowUseCase
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.usecase.GetCurrentUserFlowUseCase
import com.upsaclay.gedoise.data.NavigationItem
import com.upsaclay.gedoise.data.NavigationItemType
import kotlinx.coroutines.flow.Flow

class MainViewModel(
    isAuthenticatedFlowUseCase: IsAuthenticatedFlowUseCase,
    getCurrentUserFlowUseCase: GetCurrentUserFlowUseCase
): ViewModel() {
    val navigationItem: Map<NavigationItemType, NavigationItem> = mapOf(
        NavigationItemType.HOME to NavigationItem.Home(),
        NavigationItemType.MESSAGE to NavigationItem.Message(),
        NavigationItemType.CALENDAR to NavigationItem.Calendar(),
        NavigationItemType.FORUM to NavigationItem.Forum()
    )
    val isAuthenticated: Flow<Boolean> = isAuthenticatedFlowUseCase()
    val user: Flow<User> = getCurrentUserFlowUseCase()
}