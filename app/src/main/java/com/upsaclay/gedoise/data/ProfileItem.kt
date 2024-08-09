package com.upsaclay.gedoise.data

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.upsaclay.common.ui.theme.GedoiseColor

data class ProfileItem(
    val label: String,
    @DrawableRes val iconRes: Int,
    val iconDescription: String,
    val color: Color = GedoiseColor.BlackIconColor,
    val action: () -> Unit
)