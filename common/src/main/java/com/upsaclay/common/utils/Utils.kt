package com.upsaclay.common.utils

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
    val body = response.errorBody()?.string() ?: "No error body"
    return "Error request : $message\n" +
            "HTTP status: ${response.code()}\n" +
            "Body: $body"
}

fun formatProfilePictureUrl(fileName: String, imageExtension: String): String {
    return "https://objectstorage.eu-paris-1.oraclecloud.com/n/ax5bfuffglob/b/bucket-gedoise/o/$fileName.$imageExtension"
}
