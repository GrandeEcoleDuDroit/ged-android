package com.upsaclay.common.domain.entity

enum class SchoolLevel(val number: Int, val value: String) {
    LEVEL_1(1, "1"),
    LEVEL_2(2, "2"),
    LEVEL_3(3, "3"),
    LEVEL_4(4, "4"),
    UNKNOWN(0, "Unknown");

    companion object {
        val all: List<SchoolLevel> = listOf(LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4)

        fun fromNumber(number: Int): SchoolLevel =
            SchoolLevel.entries.find { it.number == number } ?: UNKNOWN

        fun fromValue(value: String): SchoolLevel =
            SchoolLevel.entries.find { it.value == value } ?: UNKNOWN
    }
}