package com.upsaclay.common.domain.usecase

import java.util.UUID

class GenerateIdUseCase {
    operator fun invoke() = UUID.randomUUID().toString()
}
