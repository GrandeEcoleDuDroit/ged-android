package com.upsaclay.core.domain.usecase

import com.upsaclay.core.domain.repository.DrawableRepository

class GetDrawableUriUseCase(
    private val drawableRepository: DrawableRepository
) {
    operator fun invoke(drawableId: Int) = drawableRepository.getDrawableUri(drawableId)
}