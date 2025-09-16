package com.upsaclay.common.data.remote.model

import com.google.firebase.firestore.PropertyName
import com.upsaclay.common.data.UserField

internal data class FirestoreUser(
    @get:PropertyName(UserField.Firestore.USER_ID)
    @set:PropertyName(UserField.Firestore.USER_ID)
    var userId: String = "",

    @get:PropertyName(UserField.Firestore.FIRST_NAME)
    @set:PropertyName(UserField.Firestore.FIRST_NAME)
    var firstName: String = "",

    @get:PropertyName(UserField.Firestore.LAST_NAME)
    @set:PropertyName(UserField.Firestore.LAST_NAME)
    var lastName: String = "",

    @get:PropertyName(UserField.Firestore.FULL_NAME)
    @set:PropertyName(UserField.Firestore.FULL_NAME)
    var fullName: String = "",

    @get:PropertyName(UserField.Firestore.EMAIL)
    @set:PropertyName(UserField.Firestore.EMAIL)
    var email: String = "",

    @get:PropertyName(UserField.Firestore.SCHOOL_LEVEL)
    @set:PropertyName(UserField.Firestore.SCHOOL_LEVEL)
    var schoolLevel: String = "",

    @get:PropertyName(UserField.Firestore.IS_MEMBER)
    @set:PropertyName(UserField.Firestore.IS_MEMBER)
    var isMember: Boolean = false,

    @get:PropertyName(UserField.Firestore.PROFILE_PICTURE_FILE_NAME)
    @set:PropertyName(UserField.Firestore.PROFILE_PICTURE_FILE_NAME)
    var profilePictureFileName: String? = null
)