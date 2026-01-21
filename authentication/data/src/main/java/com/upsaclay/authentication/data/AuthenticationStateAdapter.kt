package com.upsaclay.authentication.data

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.upsaclay.authentication.domain.entity.AuthenticationState
import java.lang.reflect.Type

object AuthenticationStateAdapter: JsonSerializer<AuthenticationState>, JsonDeserializer<AuthenticationState> {
    private val gson = Gson()

    override fun serialize(
        src: AuthenticationState,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return when (src) {
            is AuthenticationState.Authenticated -> gson.toJsonTree(src).asJsonObject
                .apply { addProperty("type", AuthenticationState.Authenticated.TYPE) }

            is AuthenticationState.Unauthenticated -> JsonObject()
                .apply { addProperty("type", AuthenticationState.Unauthenticated.TYPE) }
        }
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): AuthenticationState {
        val jsonObject = json.asJsonObject
        return when (jsonObject.get("type").asString) {
            AuthenticationState.Authenticated.TYPE -> gson.fromJson(jsonObject, AuthenticationState.Authenticated::class.java)

            AuthenticationState.Unauthenticated.TYPE -> AuthenticationState.Unauthenticated

            else -> throw JsonParseException("Unrecognized authentication state type")
        }
    }
}