package com.upsaclay.news.domain

import com.upsaclay.news.data.AnnouncementRepository
import com.upsaclay.news.data.model.Announcement
import kotlinx.coroutines.flow.Flow

class GetAllAnnouncementUseCase(
    private val announcementRepository: AnnouncementRepository
) {
    operator fun invoke(): Flow<List<Announcement>> = announcementRepository.announcements
}