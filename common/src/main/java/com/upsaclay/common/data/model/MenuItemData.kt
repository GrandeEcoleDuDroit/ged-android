package com.upsaclay.common.data.model

import androidx.compose.runtime.Composable

data class MenuItemData(
    val text: @Composable () -> Unit,
    val icon: @Composable (() -> Unit)? = null,
    val onClick: (() -> Unit)? = null
)