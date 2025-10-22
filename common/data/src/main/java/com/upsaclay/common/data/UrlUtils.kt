package com.upsaclay.common.data

object UrlUtils {
    fun formatOracleBucketUrl(fileName: String?): String? {
        if (fileName == null) return null
        return BuildConfig.ORACLE_BUCKET_URL + fileName
    }

    fun extractFileName(url: String?): String? = url?.substringAfterLast("/")
}