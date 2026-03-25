package com.upsaclay.news.data.announcement

internal object AnnouncementField {
    internal object Local {
        const val ANNOUNCEMENT_TABLE_NAME = "announcements"
        const val ANNOUNCEMENT_ID = "announcement_id"
        const val ANNOUNCEMENT_TITLE = "announcement_title"
        const val ANNOUNCEMENT_CONTENT = "announcement_content"
        const val ANNOUNCEMENT_DATE = "announcement_date"
        const val ANNOUNCEMENT_STATE = "announcement_state"
        const val ANNOUNCEMENT_AUTHOR_ID = "announcement_author_id"
        const val ANNOUNCEMENT_AUTHOR_FIRST_NAME = "announcement_author_first_name"
        const val ANNOUNCEMENT_AUTHOR_LAST_NAME = "announcement_author_last_name"
        const val ANNOUNCEMENT_AUTHOR_EMAIL = "announcement_author_email"
        const val ANNOUNCEMENT_AUTHOR_SCHOOL_LEVEL = "announcement_author_school_level"
        const val ANNOUNCEMENT_AUTHOR_ADMIN = "announcement_author_admin"
        const val ANNOUNCEMENT_AUTHOR_PROFILE_PICTURE_FILE_NAME = "announcement_author_profile_picture_file_name"
        const val ANNOUNCEMENT_AUTHOR_STATE = "announcement_author_state"
        const val ANNOUNCEMENT_AUTHOR_TESTER = "announcement_author_tester"
    }

    internal object Remote {
        const val ANNOUNCEMENT_ID = "ANNOUNCEMENT_ID"
        const val ANNOUNCEMENT_TITLE = "ANNOUNCEMENT_TITLE"
        const val ANNOUNCEMENT_CONTENT = "ANNOUNCEMENT_CONTENT"
        const val ANNOUNCEMENT_DATE = "ANNOUNCEMENT_DATE"
        const val USER_ID = "USER_ID"
        const val USER_FIRST_NAME = "USER_FIRST_NAME"
        const val USER_LAST_NAME = "USER_LAST_NAME"
        const val USER_EMAIL = "USER_EMAIL"
        const val USER_SCHOOL_LEVEL = "USER_SCHOOL_LEVEL"
        const val USER_ADMIN = "USER_ADMIN"
        const val USER_PROFILE_PICTURE_FILE_NAME = "USER_PROFILE_PICTURE_FILE_NAME"
        const val USER_STATE = "USER_STATE"
        const val USER_TESTER = "USER_TESTER"
    }
}