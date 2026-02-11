package com.upsaclay.news.data.post

internal object PostField {
    internal object Local {
        const val POST_TABLE_NAME = "posts"
        const val POST_ID = "post_id"
        const val POST_TITLE = "post_title"
        const val POST_CONTENT = "post_content"
        const val POST_LINK = "post_link"
        const val POST_SOURCE_ID = "post_source_id"
        const val POST_DATE = "post_date"
        const val POST_IMAGE_FILE_NAMES = "post_image_file_names"
        const val POST_STATE = "post_state"
    }

    internal object Remote {
        const val POST_ID = "POST_ID"
        const val POST_TITLE = "POST_TITLE"
        const val POST_CONTENT = "POST_CONTENT"
        const val POST_LINK = "POST_LINK"
        const val POST_SOURCE_ID = "POST_SOURCE_ID"
        const val POST_DATE = "POST_DATE"
        const val POST_IMAGE_FILE_NAMES = "POST_IMAGE_FILE_NAMES"
    }
}