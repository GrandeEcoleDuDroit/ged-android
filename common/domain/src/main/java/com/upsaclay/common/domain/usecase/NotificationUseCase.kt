package com.upsaclay.common.domain.usecase

abstract class NotificationUseCase<T> {
    abstract suspend fun sendNotification(data: T)
}