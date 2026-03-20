package com.upsaclay.common.extension

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.upsaclay.common.presentation.theme.padding

@Composable
fun Arrangement.extraSmallSpacing(): Arrangement.HorizontalOrVertical =
    spacedBy(MaterialTheme.padding.extraSmall)

@Composable
fun Arrangement.smallSpacing(): Arrangement.HorizontalOrVertical =
    spacedBy(MaterialTheme.padding.small)

@Composable
fun Arrangement.smallMediumSpacing(): Arrangement.HorizontalOrVertical =
    spacedBy(MaterialTheme.padding.smallMedium)

@Composable
fun Arrangement.mediumSpacing(): Arrangement.HorizontalOrVertical =
    spacedBy(MaterialTheme.padding.medium)

@Composable
fun Arrangement.largeSpacing(): Arrangement.HorizontalOrVertical =
    spacedBy(MaterialTheme.padding.large)