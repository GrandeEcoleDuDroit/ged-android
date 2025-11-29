package com.upsaclay.common.data

object UrlUtils {
    fun formatOracleBucketUrl(fileName: String?): String? {
        if (fileName == null) return null
        return "${BuildConfig.ORACLE_BUCKET_URL}/$fileName"
    }

    fun extractFileNameFromUrl(url: String?): String? = url?.substringAfterLast("/")

    fun extractFileNameFromPath(path: String?): String? = path?.substringAfterLast("/")
}