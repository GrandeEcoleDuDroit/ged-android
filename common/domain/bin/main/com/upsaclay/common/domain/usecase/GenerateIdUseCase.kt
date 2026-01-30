package com.upsaclay.common.domain.usecase

import java.util.UUID

class GenerateIdUseCase {
    fun execute() = UUID.randomUUID().toString()
}
