package com.upsaclay.common.domain.entity

interface Route

interface MainRoute: Route

data class RouteToNavigate(
    val mainRoute: Route,
    val routes: List<Route>
)