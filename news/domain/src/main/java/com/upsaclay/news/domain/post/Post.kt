package com.upsaclay.news.domain.post

import java.time.LocalDateTime

data class Post(
    val id: String,
    val title: String,
    val content: String?,
    val link: String,
    val source: PostSource,
    val date: LocalDateTime,
    val state: PostState
) {
    enum class PostSource(val id: Int, val label: String) {
        LINKEDIN(1, "LinkedIn"),
        INSTAGRAM(2, "Instagram"),
        WEB(3, "Web");

        companion object {
            fun fromId(id: Int): PostSource = entries.first { it.id == id }
        }
    }

    sealed class PostState {
        data object Draft : PostState() {
            override fun toString(): String = TYPE
            const val TYPE = "DRAFT"
        }

        data class Publishing(val imagePaths: List<String> = emptyList()) : PostState() {
            override fun toString(): String = TYPE

            companion object {
                const val TYPE = "PUBLISHING"
            }
        }

        data class Published(val imageUrls: List<String> = emptyList()) : PostState() {
            override fun toString(): String = TYPE

            companion object {
                const val TYPE = "PUBLISHED"
            }
        }

        data class Error(val imagePaths: List<String> = emptyList()) : PostState() {
            override fun toString(): String = TYPE

            companion object {
                const val TYPE = "ERROR"
            }
        }

        val imageReferenceValues: List<String>
            get() = when (this) {
                is Draft -> emptyList()
                is Publishing -> imagePaths
                is Published -> imageUrls
                is Error -> imagePaths
            }

        fun resolveImagePaths(): List<String> = when (this) {
            is Draft -> emptyList()
            is Publishing -> imagePaths
            is Published -> emptyList()
            is Error -> imagePaths
        }
    }
}