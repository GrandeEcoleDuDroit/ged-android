package com.upsaclay.common.presentation

import androidx.compose.runtime.Composable

data class ClickableMenuItemData(
    val text: @Composable () -> Unit,
    val icon: @Composable (() -> Unit),
    val onClick: () -> Unit
)