package com.upsaclay.news.domain

import com.upsaclay.news.domain.repository.AnnouncementRepository


class UpdateAnnouncementsUseCase(
    private val announcementRepository: AnnouncementRepository
) {
    suspend operator fun invoke(){
        announcementRepository.updateAnnouncements()
    }
}