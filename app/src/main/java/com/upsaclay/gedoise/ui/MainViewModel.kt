package com.upsaclay.gedoise.ui

import androidx.lifecycle.ViewModel
import com.upsaclay.gedoise.data.NavigationItem
import com.upsaclay.gedoise.data.NavigationItemType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel: ViewModel() {
    private val _navigationItems = MutableStateFlow(
        mapOf(
            NavigationItemType.HOME to NavigationItem.Home(),
            NavigationItemType.MESSAGE to NavigationItem.Message(),
            NavigationItemType.CALENDAR to NavigationItem.Calendar(),
            NavigationItemType.FORUM to NavigationItem.Forum()
        )
    )
    val navigationItem: StateFlow<Map<NavigationItemType, NavigationItem>> = _navigationItems.asStateFlow()

}