package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.model.ElapsedTime
import java.time.Duration
import java.time.LocalDateTime

class GetElapsedTimeUseCase {
    fun fromLocalDateTime(localDateTime: LocalDateTime): ElapsedTime {
        val duration = Duration.between(localDateTime, LocalDateTime.now())
        return if(duration.toMinutes() < 1){
            ElapsedTime.Now
        } else if(duration.toMinutes() < 60) {
            ElapsedTime.Minute(duration.toMinutes())
        } else if(duration.toHours() < 24) {
            ElapsedTime.Hour(duration.toHours())
        } else if(duration.toDays() < 7){
            ElapsedTime.Day(duration.toDays())
        } else if(duration.toDays() < 30) {
            val week = duration.toDays() / 7
            ElapsedTime.Week(week)
        } else {
            ElapsedTime.After
        }
    }
}