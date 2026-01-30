package com.upsaclay.common.domain.extensions

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

private fun LocalDateTime.toInstantUTC(): Instant = this.atZone(ZoneOffset.UTC).toInstant()

fun LocalDateTime.toEpochMilliUTC(): Long = this.toInstantUTC().toEpochMilli()

fun Instant.toLocalDateTimeUTC(): LocalDateTime = LocalDateTime.ofInstant(this, ZoneOffset.UTC)

fun Long.toLocalDateTimeUTC(): LocalDateTime = Instant.ofEpochMilli(this).toLocalDateTimeUTC()

private fun LocalDate.toInstantUTC(): Instant = this.atStartOfDay(ZoneOffset.UTC).toInstant()

fun LocalDate.toEpochMilliUTC(): Long = this.toInstantUTC().toEpochMilli()

fun Long.toLocalDateUTC(): LocalDate = Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()