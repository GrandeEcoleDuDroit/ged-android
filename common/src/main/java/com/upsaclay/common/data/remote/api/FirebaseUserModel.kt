package com.upsaclay.common.data.remote.api

import com.google.firebase.firestore.PropertyName
import com.upsaclay.common.data.model.UserDTO
import com.upsaclay.common.domain.model.User

internal data class FirebaseUserModel(
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: Int = -1,

    @get:PropertyName("first_name")
    @set:PropertyName("first_name")
    var firstName: String = "",

    @get:PropertyName("last_name")
    @set:PropertyName("last_name")
    var lastName: String = "",

    @get:PropertyName("email")
    @set:PropertyName("email")
    var email: String = "",

    @get:PropertyName("school_level")
    @set:PropertyName("school_level")
    var schoolLevel: String = "",

    @get:PropertyName("is_member")
    @set:PropertyName("is_member")
    var isMember: Boolean = false,

    @get:PropertyName("profile_picture_url")
    @set:PropertyName("profile_picture_url")
    var profilePictureUrl: String? = null,

    @get:PropertyName("is_online")
    @set:PropertyName("is_online")
    var isOnline: Boolean = false
) {
    companion object {
        fun fromDomain(user: User) = FirebaseUserModel(
            userId = user.id,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            schoolLevel = user.schoolLevel,
            isMember = user.isMember,
            profilePictureUrl = user.profilePictureUrl
        )

        fun fromDTO(userDTO: UserDTO) = FirebaseUserModel(
            userId = userDTO.userId ?: -1,
            firstName = userDTO.userFirstName,
            lastName = userDTO.userLastName,
            email = userDTO.userEmail,
            schoolLevel = userDTO.userSchoolLevel,
            isMember = userDTO.userIsMember == 1,
            profilePictureUrl = userDTO.userProfilePictureUrl
        )
    }

    fun toDomain() = User(
        id = userId,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        schoolLevel = this.schoolLevel,
        isMember = this.isMember,
        profilePictureUrl = this.profilePictureUrl
    )
}