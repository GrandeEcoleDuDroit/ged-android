package com.upsaclay.common.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    private fun toZonedDate(localDate: LocalDate): ZonedDateTime {
        return localDate.atStartOfDay(ZoneId.systemDefault())
    }

    private fun toZonedDateTime(localDateTime: LocalDateTime): ZonedDateTime {
        return localDateTime.atZone(ZoneId.systemDefault())
    }

    fun formatDayMonthYear(localDate: LocalDate): String {
        val formatter = if (Locale.getDefault().language == "fr") {
            DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.FRENCH)
        } else {
            DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
        }
        return toZonedDate(localDate).format(formatter)
    }

    fun formatDayMonthYear(localDateTime: LocalDateTime): String {
        val formatter = if (Locale.getDefault().language == "fr") {
            DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.FRENCH)
        } else {
            DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
        }
        return toZonedDateTime(localDateTime).format(formatter)
    }

    fun formatHourMinute(localDateTime: LocalDateTime): String {
        val formatter = if (Locale.getDefault().language == "fr") {
            DateTimeFormatter.ofPattern("HH:mm", Locale.FRENCH)
        } else {
            DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
        }
        return toZonedDateTime(localDateTime).format(formatter)
    }
}