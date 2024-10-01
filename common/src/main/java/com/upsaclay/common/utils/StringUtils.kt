package com.upsaclay.common.utils

import retrofit2.Response

fun <T> formatHttpError(message: String, response: Response<T>): String {
    val url = response.raw().request.url.toString()
    val method = response.raw().request.method
    val body = response.errorBody()?.string()?.replace(Regex("<[^>]*>"), "")?.trim() ?: "No error body"

    return """
        Error request: $message
        HTTP status: ${response.code()}
        URL: $url
        Method: $method
        Body: $body
    """.trimIndent()
}


fun formatProfilePictureUrl(fileName: String, imageExtension: String): String {
    return "https://objectstorage.eu-paris-1.oraclecloud.com/n/ax5bfuffglob/b/bucket-gedoise/o/$fileName.$imageExtension"
}

fun String.capitalizeFirstLetter(): String {
    return this.replaceFirstChar { it.uppercase() }
}