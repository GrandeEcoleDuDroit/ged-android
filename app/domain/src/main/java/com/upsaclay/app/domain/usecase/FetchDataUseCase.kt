package com.upsaclay.app.domain.usecase

import com.upsaclay.common.domain.usecase.FetchBlockedUsersUseCase
import com.upsaclay.mission.domain.usecase.FetchMissionsUseCase
import com.upsaclay.news.domain.usecase.FetchAnnouncementsUseCase

class FetchDataUseCase(
    private val fetchBlockedUsersUseCase: FetchBlockedUsersUseCase,
    private val fetchAnnouncementsUseCase: FetchAnnouncementsUseCase,
    private val fetchMissionsUseCase: FetchMissionsUseCase
) {
    suspend operator fun invoke() {
        try {
            fetchBlockedUsersUseCase()
            fetchAnnouncementsUseCase()
            fetchMissionsUseCase()
        } catch (e: Exception) {
            System.err.println("Error fetching data: ${e.message}")
        }
    }
}