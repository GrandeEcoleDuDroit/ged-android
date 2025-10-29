package com.upsaclay.news.data

internal object AnnouncementField {
    internal object Local {
        const val TABLE_NAME = "announcements"
        const val ANNOUNCEMENT_ID = "announcementId"
        const val TITLE = "title"
        const val CONTENT = "content"
        const val DATE = "date"
        const val STATE = "state"
        const val USER_ID = "userId"
        const val USER_FIRST_NAME = "userFirstName"
        const val USER_LAST_NAME = "userLastName"
        const val USER_EMAIL = "userEmail"
        const val USER_SCHOOL_LEVEL = "userSchoolLevel"
        const val USER_ADMIN = "userAdmin"
        const val USER_PROFILE_PICTURE_FILE_NAME = "userProfilePictureFileName"
        const val USER_STATE = "userState"
        const val USER_TESTER = "userTester"
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