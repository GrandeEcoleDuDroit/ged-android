package com.upsaclay.news.domain.post

import java.time.LocalDateTime

data class Post(
    val id: String,
    val title: String,
    val content: String,
    val link: String,
    val source: PostSource,
    val date: LocalDateTime,
    val state: PostState
) {
    enum class PostSource(val id: Int, val label: String) {
        LINKEDIN(1, "LinkedIn"),
        INSTAGRAM(2, "Instagram"),
        BLOG_LLM(3, "Blog LLM")
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
    }
}