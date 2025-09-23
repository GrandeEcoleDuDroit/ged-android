package com.upsaclay.common.domain.entity

import java.time.LocalDateTime

sealed class BlockUserEvent(
    open val userId: String,
    open val date: LocalDateTime
) {
    data class Block(override val userId: String): BlockUserEvent(userId, LocalDateTime.now())
    data class Unblock(override val userId: String): BlockUserEvent(userId, LocalDateTime.now())
}