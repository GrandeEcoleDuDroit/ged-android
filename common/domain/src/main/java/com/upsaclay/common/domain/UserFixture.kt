package com.upsaclay.common.domain

import com.upsaclay.common.domain.entity.BlockedUser
import com.upsaclay.common.domain.entity.Reporter
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import java.time.LocalDateTime

val userFixture = User(
    id = "1",
    firstName = "Jean",
    lastName = "Dupont",
    email = "jean.dupont@email.com",
    schoolLevel = SchoolLevel.LEVEL_1,
    admin = true,
    profilePictureUrl = "https://images.unsplash.com/photo-1545570503-b656623ef132?q=80&w=1364&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
)

val userFixture2 = User(
    id = "2",
    firstName = "Patrick",
    lastName = "Boucher",
    email = "patrick.boucher@email.com",
    schoolLevel = SchoolLevel.LEVEL_2,
    profilePictureUrl = "https://cdn.pixabay.com/photo/2023/07/25/19/27/ai-generated-8149775_1280.jpg"
)

val userFixture3 = User(
    id = "3",
    firstName = "Evelyne",
    lastName = "Aubin",
    email = "evelyne.aubin@email.com",
    schoolLevel = SchoolLevel.LEVEL_3,
    profilePictureUrl = "https://images.unsplash.com/photo-1596854307809-6e754c522f95?q=80&w=1760&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
)


val usersFixture = listOf(
    userFixture,
    userFixture.copy(
        id = "2",
        firstName = "François",
        lastName = "Martin",
        profilePictureUrl = "https://images.unsplash.com/photo-1459356979461-dae1b8dcb702?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    ),
    userFixture.copy(
        id = "3",
        firstName = "Sonia",
        lastName = "Delaunay",
        profilePictureUrl = "https://images.unsplash.com/photo-1552728089-57bdde30beb3?q=80&w=1325&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    ),
    userFixture.copy(
        id = "4",
        firstName = "Pedro",
        lastName = "Sanchez",
        profilePictureUrl = "https://images.unsplash.com/photo-1563313003-a39f4d54499d?q=80&w=1335&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    ),
    userFixture.copy(
        id = "5",
        firstName = "Élodie",
        lastName = "LeFevre",
        profilePictureUrl = "https://images.unsplash.com/photo-1503454537195-1dcabb73ffb9?q=80&w=1286&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    ),
    userFixture.copy(
        id = "6",
        firstName = "Rémy",
        lastName = "Roy",
        profilePictureUrl = "https://plus.unsplash.com/premium_photo-1670596899123-c4c67735d77a?q=80&w=2340&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    ),
    userFixture.copy(
        id = "7",
        firstName = "Louis",
        lastName = "Leclerc",
        profilePictureUrl = "https://images.unsplash.com/photo-1745758278435-db28ef18d6b2?q=80&w=1374&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
    )
)

val blockedUserFixture = BlockedUser("userId", LocalDateTime.now())
val blockedUsersFixture = mapOf(blockedUserFixture.userId to blockedUserFixture)

val reporterFixture = Reporter(
    fullName = userFixture.fullName,
    email = userFixture.email
)