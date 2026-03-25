package com.upsaclay.news.data.post

import com.upsaclay.common.data.BuildConfig
import com.upsaclay.news.domain.post.PostUtils

fun PostUtils.Image.formatUrl(fileName: String?): String? =
    fileName?.let {
        "${BuildConfig.ORACLE_BUCKET_URL}/${getRelativePath(fileName)}"
    }

fun PostUtils.Image.extractFileNameFromPath(path: String?): String? = path?.substringAfterLast("/")