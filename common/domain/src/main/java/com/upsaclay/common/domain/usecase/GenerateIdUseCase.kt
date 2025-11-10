package com.upsaclay.common.domain.usecase

import java.util.UUID

object GenerateIdUseCase {
    operator fun invoke() = UUID.randomUUID().toString()
}
