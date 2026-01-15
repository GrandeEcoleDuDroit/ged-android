package com.upsaclay.common.domain.entity

import java.time.LocalDateTime

data class BlockedUser(
    val userId: String,
    val blockedDate: LocalDateTime
)