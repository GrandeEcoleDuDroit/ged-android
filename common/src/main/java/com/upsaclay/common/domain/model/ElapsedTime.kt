package com.upsaclay.common.domain.model

sealed class ElapsedTime {
    data object Now: ElapsedTime()
    data class Minute(val value: Long) : ElapsedTime()
    data class Hour(val value: Long) : ElapsedTime()
    data class Day(val value: Long) : ElapsedTime()
    data class Week(val value: Long): ElapsedTime()
    data object After: ElapsedTime()
}