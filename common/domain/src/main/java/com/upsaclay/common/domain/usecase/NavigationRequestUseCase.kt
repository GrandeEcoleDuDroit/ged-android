package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.entity.Route
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update

class NavigationRequestUseCase {
    private val _routesToNavigate = MutableStateFlow<List<Route>?>(null)
    val routesToNavigate: Flow<List<Route>> = _routesToNavigate.filterNotNull()

    fun navigate(routesToNavigate: List<Route>) {
        _routesToNavigate.update { routesToNavigate }
    }

    fun resetRoute() {
        _routesToNavigate.update { null }
    }
}