package com.upsaclay.common.domain

import com.google.gson.Gson
import com.upsaclay.common.domain.entity.User

object UserJsonParser {
    private val gson = Gson()

    fun toUser(userJson: String): User? = gson.fromJson(userJson, User::class.java)

    fun toJson(user: User): String = gson.toJson(user)
}