package com.upsaclay.common.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.upsaclay.common.domain.model.User
import retrofit2.Response

val userFixture = User(
    12,
    "Pierre",
    "Dupont",
    "pierre.dupont@universite-paris-saclay.fr",
    "GED 1",
    false,
    "https://i-mom.unimedias.fr/2020/09/16/dragon-ball-songoku.jpg"
)

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

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun showToast(context: Context, @StringRes stringRes: Int) {
    Toast.makeText(context, stringRes, Toast.LENGTH_SHORT).show()
}