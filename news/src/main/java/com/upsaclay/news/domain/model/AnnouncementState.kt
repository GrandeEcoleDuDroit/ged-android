package com.upsaclay.news.domain.model

enum class AnnouncementState {
    DEFAULT,
    LOADING,
    ANNOUNCEMENT_CREATED,
    ANNOUNCEMENT_DELETED,
    ANNOUNCEMENT_CREATION_ERROR,
    ANNOUNCEMENT_DISPLAY_ERROR
}