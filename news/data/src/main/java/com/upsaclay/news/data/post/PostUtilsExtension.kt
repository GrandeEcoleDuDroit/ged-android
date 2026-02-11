package com.upsaclay.news.data.post

import com.upsaclay.news.domain.post.PostUtils

fun PostUtils.Image.extractFileNameFromPath(path: String?): String? = path?.substringAfterLast("/")