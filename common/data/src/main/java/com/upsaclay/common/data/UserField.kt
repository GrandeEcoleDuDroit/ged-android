package com.upsaclay.common.data

object UserField {
    internal object Local {
        const val USER_ID = "user_Id"
        const val USER_FIRST_NAME = "user_first_name"
        const val USER_LAST_NAME = "user_last_name"
        const val USER_EMAIL = "user_email"
        const val USER_SCHOOL_LEVEL = "user_school_level"
        const val USER_ADMIN = "user_admin"
        const val USER_PROFILE_PICTURE_FILE_NAME = "user_profile_picture_file_name"
        const val USER_STATE = "user_state"
        const val USER_TESTER = "user_tester"
    }

    object Oracle {
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

    internal object Firestore {
        const val TABLE_NAME = "users"
        const val USER_ID = "userId"
        const val FIRST_NAME = "firstName"
        const val LAST_NAME = "lastName"
        const val EMAIL = "email"
        const val SCHOOL_LEVEL = "schoolLevel"
        const val ADMIN = "admin"
        const val PROFILE_PICTURE_FILE_NAME = "profilePictureFileName"
        const val STATE = "state"
        const val TESTER = "tester"
    }
}

object BlockedUserField {
    internal object Remote {
        const val USER_ID = "USER_ID"
        const val BLOCKED_USER_ID = "BLOCKED_USER_ID"
        const val BLOCKED_DATE = "BLOCKED_DATE"
    }
}