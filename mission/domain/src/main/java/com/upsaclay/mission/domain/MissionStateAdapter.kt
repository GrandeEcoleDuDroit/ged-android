package com.upsaclay.mission.domain

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.upsaclay.mission.domain.entity.MissionState
import java.lang.reflect.Type

object MissionStateAdapter: JsonSerializer<MissionState>, JsonDeserializer<MissionState> {
    override fun serialize(
        src: MissionState,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return when (src) {
            is MissionState.Draft -> context.serialize(src).asJsonObject
                .apply { addProperty("type", MissionState.DRAFT) }

            is MissionState.Publishing -> context.serialize(src).asJsonObject
                .apply { addProperty("type", MissionState.PUBLISHING) }

            is MissionState.Published -> context.serialize(src).asJsonObject
                .apply { addProperty("type", MissionState.PUBLISHED) }

            is MissionState.Error -> context.serialize(src).asJsonObject
                .apply { addProperty("type", MissionState.ERROR) }
        }
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): MissionState {
        return when (val type = json.asJsonObject.get("type").asString) {
            MissionState.DRAFT -> context.deserialize(json, MissionState.Draft::class.java)
            MissionState.PUBLISHING -> context.deserialize(json, MissionState.Publishing::class.java)
            MissionState.PUBLISHED -> context.deserialize(json, MissionState.Published::class.java)
            MissionState.ERROR -> context.deserialize(json, MissionState.Error::class.java)
            else -> throw JsonParseException("Unrecognized mission state type : $type")
        }
    }
}