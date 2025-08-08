package com.upsaclay.gedoise.data.repository

import com.upsaclay.common.domain.entity.Route
import com.upsaclay.common.domain.repository.RouteRepository
import com.upsaclay.gedoise.domain.repository.SendMailRepository

internal class RouteRepositoryImpl: RouteRepository {
    private var _currentRoute: Route? = null
    override val currentRoute: Route? get() = _currentRoute

    override fun setCurrentRoute(route: Route?) {
        _currentRoute = route
    }
}

