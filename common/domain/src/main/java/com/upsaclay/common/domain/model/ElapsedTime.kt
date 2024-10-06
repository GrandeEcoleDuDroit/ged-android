package com.upsaclay.common.domain.model

import java.time.LocalDateTime

sealed class ElapsedTime {
    data class Now(val value: Long) : com.upsaclay.common.domain.model.ElapsedTime()

    data class Minute(val value: Long) : com.upsaclay.common.domain.model.ElapsedTime()

    data class Hour(val value: Long) : com.upsaclay.common.domain.model.ElapsedTime()

    data class Day(val value: Long) : com.upsaclay.common.domain.model.ElapsedTime()

    data class Week(val value: Long) : com.upsaclay.common.domain.model.ElapsedTime()

    data class Later(val value: LocalDateTime) : com.upsaclay.common.domain.model.ElapsedTime()
}