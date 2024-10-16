package com.upsaclay.common.data.model

import com.upsaclay.common.domain.model.User

internal object UserMapper {
    fun toDTO(user: User) = UserDTO(
        userId = if (user.id == -1) null else user.id,
        userFirstName = user.firstName,
        userLastName = user.lastName,
        userEmail = user.email,
        userSchoolLevel = user.schoolLevel,
        userIsMember = if (user.isMember) 1 else 0,
        userProfilePictureUrl = user.profilePictureUrl
    )

    fun toDomain(userDTO: UserDTO) = User(
        id = userDTO.userId ?: -1,
        firstName = userDTO.userFirstName,
        lastName = userDTO.userLastName,
        email = userDTO.userEmail,
        schoolLevel = userDTO.userSchoolLevel,
        isMember = userDTO.userIsMember == 1,
        profilePictureUrl = userDTO.userProfilePictureUrl
    )
}