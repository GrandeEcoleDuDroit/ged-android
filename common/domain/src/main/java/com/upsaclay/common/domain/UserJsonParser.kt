package com.upsaclay.common.domain

import com.google.gson.Gson
import com.upsaclay.common.domain.entity.User

object UserJsonParser {
    fun fromJson(userJson: String): User? {
        return runCatching {
            Gson().fromJson(userJson, User::class.java)
        }.getOrNull()
    }

    fun toJson(user: User): String {
        return Gson().toJson(user)
    }
}