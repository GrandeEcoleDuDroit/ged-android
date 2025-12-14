package com.upsaclay.common.data.extensions

import com.upsaclay.common.data.BuildConfig
import com.upsaclay.common.domain.UserUtils

fun UserUtils.ProfilePicture.formatUrl(fileName: String?): String? =
    fileName?.let {
        "${BuildConfig.ORACLE_BUCKET_URL}/${makeRelativePath(fileName)}"
    }