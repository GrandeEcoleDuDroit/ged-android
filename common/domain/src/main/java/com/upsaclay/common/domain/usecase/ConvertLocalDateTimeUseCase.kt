package com.upsaclay.common.domain.usecase

import java.time.LocalDateTime
import java.time.ZoneId

class ConvertLocalDateTimeUseCase {
    fun toTimestamp(localDateTime: LocalDateTime): Long = localDateTime.toEpochSecond(ZoneId.systemDefault().rules.getOffset(localDateTime))
}