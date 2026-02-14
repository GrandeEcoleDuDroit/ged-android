package com.upsaclay.news.presentation.post

import com.google.gson.GsonBuilder
import com.upsaclay.common.domain.adapter.LocalDateTimeAdapter
import com.upsaclay.news.domain.post.Post
import java.time.LocalDateTime

object PostGsonParser {
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter)
        .registerTypeAdapter(Post.PostState::class.java, PostStateGsonAdapter)
        .create()

    fun toPost(postJson: String): Post = gson.fromJson(postJson, Post::class.java)

    fun toJson(mission: Post): String = gson.toJson(mission)
}