package com.upsaclay.common.domain.entity

sealed class BlockUserEvent(open val userId: String) {
    data class Block(val blockedUser: BlockedUser): BlockUserEvent(blockedUser.userId)
    data class Unblock(val blockedUserId: String): BlockUserEvent(blockedUserId)
}