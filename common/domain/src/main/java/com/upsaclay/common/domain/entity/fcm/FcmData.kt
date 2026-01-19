package com.upsaclay.common.domain.entity.fcm

import com.google.gson.annotations.SerializedName

data class FcmData<T>(
    val type: FcmDataType,
    val value: T
)

enum class FcmDataType {
    @SerializedName("message")
    MESSAGE;

    override fun toString(): String {
        return when (this) {
            MESSAGE -> "message"
        }
    }

    companion object {
        fun fromString(value: String): FcmDataType? {
            return when (value) {
                "message" -> MESSAGE
                else -> null
            }
        }
    }
}