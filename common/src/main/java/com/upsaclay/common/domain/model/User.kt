package com.upsaclay.common.domain.model

import com.upsaclay.common.data.model.UserDTO

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val schoolLevel: String,
    val isMember: Boolean,
    val profilePictureUrl: String
) {
    val fullName = String.format("%s %s", firstName, lastName)

    fun toDTO() = UserDTO(
        userId = if(id == -1) null else id,
        userFirstName = firstName,
        userLastName = lastName,
        userEmail = email,
        userSchoolLevel = schoolLevel,
        userIsMember = if(isMember) 1 else 0,
        profilePictureUrl = profilePictureUrl
    )

    companion object {
        fun fromDTO(userDTO: UserDTO) = User(
            id = userDTO.userId ?: 0,
            firstName = userDTO.userFirstName,
            lastName = userDTO.userLastName,
            email = userDTO.userEmail,
            schoolLevel = userDTO.userSchoolLevel,
            isMember = userDTO.userIsMember == 1,
            profilePictureUrl = userDTO.profilePictureUrl
        )
    }
}
