package com.upsaclay.common.domain.entity

enum class SchoolLevel(val number: Int, val value: String) {
    GED_1(1, "GED 1"),
    GED_2(2, "GED 2"),
    GED_3(3, "GED 3"),
    GED_4(4, "GED 4"),
    UNKNOWN(0, "Unknown");

    companion object {
        val all: List<SchoolLevel> = listOf(GED_1, GED_2, GED_3, GED_4)

        fun fromNumber(number: Int): SchoolLevel =
            SchoolLevel.entries.find { it.number == number } ?: UNKNOWN

        fun fromValue(value: String): SchoolLevel =
            SchoolLevel.entries.find { it.value == value } ?: UNKNOWN
    }
}