package com.upsaclay.common.utils

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object FormatLocalDateHelper {
    private fun toZonedDate(localDate: LocalDate): ZonedDateTime {
        return localDate.atStartOfDay(ZoneId.systemDefault())
    }

    fun formatDayMonthYear(localDate: LocalDate): String {
        val formatter = if (Locale.getDefault().language == "fr") {
            DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.FRENCH)
        } else {
            DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
        }
        return toZonedDate(localDate).format(formatter)
    }
}