package com.upsaclay.mission.presentation

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.upsaclay.mission.domain.entity.Mission.MissionState
import java.lang.reflect.Type

object MissionGsonStateAdapter: JsonSerializer<MissionState>, JsonDeserializer<MissionState> {
    override fun serialize(
        src: MissionState,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return when (src) {
            is MissionState.Draft -> context.serialize(src).asJsonObject
                .apply { addProperty("type", MissionState.Draft.TYPE) }

            is MissionState.Publishing -> context.serialize(src).asJsonObject
                .apply { addProperty("type", MissionState.Publishing.TYPE) }

            is MissionState.Published -> context.serialize(src).asJsonObject
                .apply { addProperty("type", MissionState.Published.TYPE) }

            is MissionState.Error -> context.serialize(src).asJsonObject
                .apply { addProperty("type", MissionState.Error.TYPE) }
        }
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): MissionState {
        return when (val type = json.asJsonObject.get("type").asString) {
            MissionState.Draft.TYPE -> context.deserialize(json, MissionState.Draft::class.java)
            MissionState.Publishing.TYPE -> context.deserialize(json, MissionState.Publishing::class.java)
            MissionState.Published.TYPE -> context.deserialize(json, MissionState.Published::class.java)
            MissionState.Error.TYPE -> context.deserialize(json, MissionState.Error::class.java)
            else -> throw JsonParseException("Unrecognized mission state type : $type")
        }
    }
}