package com.upsaclay.news.data.post.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.upsaclay.news.data.post.PostField.Local.POST_CONTENT
import com.upsaclay.news.data.post.PostField.Local.POST_DATE
import com.upsaclay.news.data.post.PostField.Local.POST_ID
import com.upsaclay.news.data.post.PostField.Local.POST_IMAGE_FILE_NAMES
import com.upsaclay.news.data.post.PostField.Local.POST_LINK
import com.upsaclay.news.data.post.PostField.Local.POST_SOURCE_ID
import com.upsaclay.news.data.post.PostField.Local.POST_STATE
import com.upsaclay.news.data.post.PostField.Local.POST_TABLE_NAME
import com.upsaclay.news.data.post.PostField.Local.POST_TITLE

@Entity(tableName = POST_TABLE_NAME)
data class LocalPost(
    @PrimaryKey
    @ColumnInfo(name = POST_ID)
    val postId: String,
    @ColumnInfo(name = POST_TITLE)
    val postTitle: String,
    @ColumnInfo(name = POST_CONTENT)
    val postContent: String?,
    @ColumnInfo(name = POST_LINK)
    val postLink: String,
    @ColumnInfo(name = POST_SOURCE_ID)
    val postSourceId: Int,
    @ColumnInfo(name = POST_DATE)
    val postDate: Long,
    @ColumnInfo(name = POST_IMAGE_FILE_NAMES)
    val postImageFileNames: String,
    @ColumnInfo(name = POST_STATE)
    val postState: String
)
