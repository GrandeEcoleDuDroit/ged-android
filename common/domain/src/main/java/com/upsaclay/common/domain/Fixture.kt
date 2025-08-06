

package com.upsaclay.common.domain

import com.upsaclay.common.domain.entity.FcmToken
import com.upsaclay.common.domain.entity.User


val userFixture = User(
    "12",
    "Pierre",
    "Dupont",
    "pierre.dupont@universite-paris-saclay.fr",
    "GED 1",
    true,
    "https://i-mom.unimedias.fr/2020/09/16/dragon-ball-songoku.jpg"
)

val userFixture2 = User(
    "13",
    "Alain",
    "Robert",
    "alain.robert@universite-paris-saclay.fr",
    "GED 3",
    false,
    "https://avatarfiles.alphacoders.com/330/330775.png"
)

val usersFixture = listOf(
    userFixture,
    userFixture2,
    userFixture.copy(
        id = "14",
        firstName = "Marie",
        lastName = "Curie",
        email = "marie@email.com"
    ),
    userFixture.copy(
        id = "15",
        firstName = "Albert",
        lastName = "Einstein",
        email = "albert@email.com"
    ),
    userFixture.copy(
        id = "16",
        firstName = "Isaac",
        lastName = "Newton",
        email = "issac@email.com"
    ),
    userFixture.copy(
        id = "17",
        firstName = "Galileo",
        lastName = "Galilei",
        email = "galil@email.com"
    ),
    userFixture.copy(
        id = "18",
        firstName = "Ada",
        lastName = "Lovelace",
        email = "ada@email.com"
    ),
    userFixture.copy(
        id = "19",
        firstName = "Charles",
        lastName = "Darwin",
        email = "charles@email.com"
    ),
)
val fcmTokenFixture = FcmToken(userFixture.id, "token")