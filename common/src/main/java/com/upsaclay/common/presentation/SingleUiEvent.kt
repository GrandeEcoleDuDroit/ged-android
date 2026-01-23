package com.upsaclay.common.presentation

import androidx.annotation.StringRes

interface SingleUiEvent {
    data class Success(@StringRes val messageId: Int = -1): SingleUiEvent
    data class Error(@StringRes val messageId: Int): SingleUiEvent
}