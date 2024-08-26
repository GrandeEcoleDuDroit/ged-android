package com.upsaclay.common.domain.usecase

import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class ConvertTimestampUseCase {
    fun toLocalDateTime(timestamp: Long): LocalDateTime {
        return LocalDateTime
            .ofEpochSecond(timestamp, 0, ZoneOffset.ofHours(2))
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    fun toLocalDateTime(timestamp: Timestamp): LocalDateTime {
        return LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault())
    }
}