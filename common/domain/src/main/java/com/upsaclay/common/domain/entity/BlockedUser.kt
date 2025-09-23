package com.upsaclay.common.domain.entity

import java.time.LocalDateTime

data class BlockedUser(
    val id: String,
    val blockedAt: LocalDateTime
)