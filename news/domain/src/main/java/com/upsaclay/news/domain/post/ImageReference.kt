package com.upsaclay.news.domain.post

sealed class ImageReference(open val value: String) {
    data class ImageUrl(override val value: String): ImageReference(value)
    data class ImageUri(override val value: String): ImageReference(value)
}
