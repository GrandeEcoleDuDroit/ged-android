package com.upsaclay.news.domain.usecase

import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.news.domain.entity.AnnouncementState
import com.upsaclay.news.domain.repository.AnnouncementRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber

private const val DEBOUNCE_INTERVAL = 10000L

class RefreshAnnouncementUseCase(
    private val announcementRepository: AnnouncementRepository,
    private val connectivityObserver: ConnectivityObserver,
    private val blockedUserRepository: BlockedUserRepository,
    scope: CoroutineScope
) {
    internal var lastRequestTime: Long = 0

    init {
        scope.launch {
            try {
                invoke()
            } catch (e: Exception) {
                Timber.e(e, "Failed to refresh announcements: ${e.message}")
            }
        }
    }

    suspend operator fun invoke() {
        if (!connectivityObserver.isConnected) {
            throw NoInternetConnectionException()
        }

        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRequestTime > DEBOUNCE_INTERVAL) {
            refresh()
            lastRequestTime = currentTime
        }
    }

    private suspend fun refresh() {
        val announcements = announcementRepository.announcements.firstOrNull() ?: emptyList()
        val remoteAnnouncements = announcementRepository.getRemoteAnnouncements()
        val blockedUserIds = blockedUserRepository.getLocalBlockedUserIds()

        val announcementsToDelete = announcements.filter {
            (it.state == AnnouncementState.PUBLISHED && it !in remoteAnnouncements) ||
            it.author.id in blockedUserIds
        }
        val announcementsToUpsert = remoteAnnouncements.filter {
            it !in announcements && it.author.id !in blockedUserIds
        }

        announcementsToDelete.forEach { announcementRepository.deleteLocalAnnouncement(it) }
        announcementsToUpsert.forEach { announcementRepository.upsertLocalAnnouncement(it) }
    }
}