package com.upsaclay.gedoise.domain.usecase

import com.upsaclay.common.domain.usecase.SynchronizeBlockedUserUseCase

class SynchronizeDataUseCase(
    private val synchronizeBlockedUserUseCase: SynchronizeBlockedUserUseCase
) {
    suspend fun synchronize() {
        synchronizeBlockedUserUseCase.synchronize()
    }
}