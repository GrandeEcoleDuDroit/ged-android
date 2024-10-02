package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.model.ElapsedTime
import java.time.Duration
import java.time.LocalDateTime

class GetElapsedTimeUseCase {
    fun fromLocalDateTime(localDateTime: LocalDateTime): com.upsaclay.common.domain.model.ElapsedTime {
        val duration = Duration.between(localDateTime, LocalDateTime.now())
        return when {
            duration.toMinutes() < 1 -> com.upsaclay.common.domain.model.ElapsedTime.Now(duration.seconds)
            duration.toMinutes() < 60 -> com.upsaclay.common.domain.model.ElapsedTime.Minute(duration.toMinutes())
            duration.toHours() < 24 -> com.upsaclay.common.domain.model.ElapsedTime.Hour(duration.toHours())
            duration.toDays() < 7 -> com.upsaclay.common.domain.model.ElapsedTime.Day(duration.toDays())
            duration.toDays() < 30 -> com.upsaclay.common.domain.model.ElapsedTime.Week(duration.toDays() / 7)
            else -> com.upsaclay.common.domain.model.ElapsedTime.Later(localDateTime)
        }
    }
}