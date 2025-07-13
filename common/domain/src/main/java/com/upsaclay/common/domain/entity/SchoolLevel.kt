package com.upsaclay.common.domain.entity

enum class SchoolLevel {
    GED_1,
    GED_2,
    GED_3,
    GED_4;

    companion object {
        fun fromString(value: String): SchoolLevel? = SchoolLevel.entries.find { it.name == value }
    }

    override fun toString(): String {
        return when (this) {
            GED_1 -> "GED 1"
            GED_2 -> "GED 2"
            GED_3 -> "GED 3"
            GED_4 -> "GED 4"
        }
    }
}