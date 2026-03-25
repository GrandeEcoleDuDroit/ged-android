package com.upsaclay.news.presentation.post

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.upsaclay.news.domain.post.Post.PostState
import java.lang.reflect.Type

object PostStateGsonAdapter: JsonSerializer<PostState>, JsonDeserializer<PostState> {
    override fun serialize(
        src: PostState,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return when (src) {
            is PostState.Draft -> context.serialize(src).asJsonObject
                .apply { addProperty("type", PostState.Draft.TYPE) }

            is PostState.Publishing -> context.serialize(src).asJsonObject
                .apply { addProperty("type", PostState.Publishing.TYPE) }

            is PostState.Published -> context.serialize(src).asJsonObject
                .apply { addProperty("type", PostState.Published.TYPE) }

            is PostState.Error -> context.serialize(src).asJsonObject
                .apply { addProperty("type", PostState.Error.TYPE) }
        }
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): PostState {
        return when (val type = json.asJsonObject.get("type").asString) {
            PostState.Draft.TYPE -> context.deserialize(json, PostState.Draft::class.java)
            PostState.Publishing.TYPE -> context.deserialize(json, PostState.Publishing::class.java)
            PostState.Published.TYPE -> context.deserialize(json, PostState.Published::class.java)
            PostState.Error.TYPE -> context.deserialize(json, PostState.Error::class.java)
            else -> throw JsonParseException("Unrecognized post state type : $type")
        }
    }
}