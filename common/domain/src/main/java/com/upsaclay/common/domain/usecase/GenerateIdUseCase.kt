package com.upsaclay.common.domain.usecase

import java.util.UUID
import kotlin.math.absoluteValue

object GenerateIdUseCase {
    operator fun invoke() = UUID.randomUUID().toString()
}
