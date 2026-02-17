package com.upsaclay.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.ElapsedTime
import com.upsaclay.common.domain.usecase.GetElapsedTimeUseCase
import java.time.LocalDateTime

@Composable
fun getElapsedTimeValue(date: LocalDateTime, format: ElapsedTimeValueFormat = ElapsedTimeValueFormat.SHORT): String {
    return when (val elapsedTime = GetElapsedTimeUseCase.fromLocalDateTime(date)) {
        is ElapsedTime.Now -> stringResource(com.upsaclay.common.R.string.now)
        is ElapsedTime.Minute -> {
            when (format) {
                ElapsedTimeValueFormat.SHORT -> stringResource(com.upsaclay.common.R.string.minute_ago_short, elapsedTime.value)
                ElapsedTimeValueFormat.LONG -> stringResource(com.upsaclay.common.R.string.minute_ago_long, elapsedTime.value)
            }
        }
        is ElapsedTime.Hour -> {
            when (format) {
                ElapsedTimeValueFormat.SHORT -> stringResource(com.upsaclay.common.R.string.hour_ago_short, elapsedTime.value)
                ElapsedTimeValueFormat.LONG -> stringResource(com.upsaclay.common.R.string.hour_ago_long, elapsedTime.value)
            }
        }
        is ElapsedTime.Day -> {
            when (format) {
                ElapsedTimeValueFormat.SHORT -> stringResource(com.upsaclay.common.R.string.day_ago_short, elapsedTime.value)
                ElapsedTimeValueFormat.LONG -> stringResource(com.upsaclay.common.R.string.day_ago_long, elapsedTime.value)
            }
        }

        is ElapsedTime.Later -> DateUtils.formatDayMonthYear(elapsedTime.value)
    }
}

enum class ElapsedTimeValueFormat {
    SHORT,
    LONG
}