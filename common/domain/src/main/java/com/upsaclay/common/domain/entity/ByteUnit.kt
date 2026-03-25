package com.upsaclay.common.domain.entity

enum class ByteUnit(val value: Long) {
    BYTE(1),
    KILO_BYTE(1 shl 10),
    MEGA_BYTE(1 shl 20),
    GIGA_BYTE(1 shl 30)
}