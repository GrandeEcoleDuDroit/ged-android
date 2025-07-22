package com.upsaclay.common.extension

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.upsaclay.common.presentation.theme.spacing

@Composable
fun Arrangement.smallSpacing(): Arrangement.HorizontalOrVertical = spacedBy(MaterialTheme.spacing.small)

@Composable
fun Arrangement.smallMediumSpacing(): Arrangement.HorizontalOrVertical = spacedBy(MaterialTheme.spacing.smallMedium)

@Composable
fun Arrangement.mediumSpacing(): Arrangement.HorizontalOrVertical = spacedBy(MaterialTheme.spacing.medium)

@Composable
fun Arrangement.mediumLargeSpacing(): Arrangement.HorizontalOrVertical = spacedBy(MaterialTheme.spacing.mediumLarge)

@Composable
fun Arrangement.largeSpacing(): Arrangement.HorizontalOrVertical = spacedBy(MaterialTheme.spacing.large)