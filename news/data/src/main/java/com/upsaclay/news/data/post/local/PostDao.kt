package com.upsaclay.news.data.post.local

import androidx.room.Dao
import androidx.room.Upsert

@Dao
interface PostDao {
    @Upsert
    suspend fun upsertPost(localPost: LocalPost)
}