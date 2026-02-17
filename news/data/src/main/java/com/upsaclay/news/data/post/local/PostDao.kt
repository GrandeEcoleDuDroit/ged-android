package com.upsaclay.news.data.post.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.upsaclay.news.data.post.PostField.Local.POST_DATE
import com.upsaclay.news.data.post.PostField.Local.POST_ID
import com.upsaclay.news.data.post.PostField.Local.POST_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM $POST_TABLE_NAME ORDER BY $POST_DATE DESC")
    fun getPostsFlow(): Flow<List<LocalPost>>

    @Query("SELECT * FROM $POST_TABLE_NAME ORDER BY $POST_DATE DESC")
    suspend fun getPosts(): List<LocalPost>

    @Query("SELECT * FROM $POST_TABLE_NAME WHERE $POST_ID = :postId")
    fun getPostFlow(postId: String): Flow<LocalPost?>

    @Query("SELECT * FROM $POST_TABLE_NAME WHERE $POST_ID = :postId")
    suspend fun getPost(postId: String): LocalPost?

    @Upsert
    suspend fun upsertPost(localPost: LocalPost)

    @Delete
    suspend fun deletePost(localPost: LocalPost)
}