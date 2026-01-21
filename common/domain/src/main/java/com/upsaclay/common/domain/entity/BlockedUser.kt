package com.upsaclay.common.domain.entity

import java.time.LocalDateTime

data class BlockedUser(
    val userId: String,
    val date: LocalDateTime
)

typealias BlockedUsers = Map<String, BlockedUser>