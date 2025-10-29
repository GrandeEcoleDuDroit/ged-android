package com.upsaclay.common.data

internal object UserField {
    internal object Local {
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

    internal object Server {
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