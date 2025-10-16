package com.upsaclay.common.domain

object UrlUtils {
    fun formatOracleBucketUrl(fileName: String?): String? {
        if (fileName == null) return null
        return "https://objectstorage.eu-paris-1.oraclecloud.com/n/ax5bfuffglob/b/bucket-gedoise/o/$fileName"
    }

    fun extractFileName(url: String?): String? = url?.substringAfterLast("/")
}