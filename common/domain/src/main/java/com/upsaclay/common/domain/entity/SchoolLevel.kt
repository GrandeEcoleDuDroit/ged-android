package com.upsaclay.common.domain.entity

enum class SchoolLevel(val number: Int, val value: String) {
    GED_1(1, "GED 1"),
    GED_2(2, "GED 2"),
    GED_3(3, "GED 3"),
    GED_4(4, "GED 4");

    companion object {
        fun fromValue(value: String): SchoolLevel? {
            return SchoolLevel.entries.find { it.toString() == value }
        }

        fun fromNumber(id: Int): SchoolLevel? {
            return SchoolLevel.entries.find { it.number == id }
        }
    }

    override fun toString(): String = value
}