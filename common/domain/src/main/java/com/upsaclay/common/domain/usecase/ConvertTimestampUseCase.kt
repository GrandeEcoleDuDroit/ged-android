package com.upsaclay.common.domain.usecase

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class ConvertTimestampUseCase {
    fun toLocalDateTime(timestamp: Long): LocalDateTime {
        return LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.ofHours(2))
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }
}