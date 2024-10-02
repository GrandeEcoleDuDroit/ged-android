package com.upsaclay.common.domain


fun formatProfilePictureUrl(fileName: String, imageExtension: String): String {
    return "https://objectstorage.eu-paris-1.oraclecloud.com/n/ax5bfuffglob/b/bucket-gedoise/o/$fileName.$imageExtension"
}

fun String.capitalizeFirstLetter(): String {
    return this.replaceFirstChar { it.uppercase() }
}