package com.upsaclay.mission.data

import com.upsaclay.common.data.BuildConfig
import com.upsaclay.mission.domain.MissionUtils

fun MissionUtils.Image.formatUrl(fileName: String?): String? =
    fileName?.let {
        "${BuildConfig.ORACLE_BUCKET_URL}/${getRelativePath(fileName)}"
    }

fun MissionUtils.Image.extractFileNameFromPath(path: String?): String? = path?.substringAfterLast("/")