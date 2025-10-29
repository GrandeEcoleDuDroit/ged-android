package com.upsaclay.common.extension

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource

@Composable
fun Arrangement.extraSmallSpacing(): Arrangement.HorizontalOrVertical =
    spacedBy(dimensionResource(com.upsaclay.common.R.dimen.extra_small_padding))

@Composable
fun Arrangement.smallSpacing(): Arrangement.HorizontalOrVertical =
    spacedBy(dimensionResource(com.upsaclay.common.R.dimen.small_padding))

@Composable
fun Arrangement.smallMediumSpacing(): Arrangement.HorizontalOrVertical =
    spacedBy(dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding))

@Composable
fun Arrangement.mediumSpacing(): Arrangement.HorizontalOrVertical =
    spacedBy(dimensionResource(com.upsaclay.common.R.dimen.medium_padding))

@Composable
fun Arrangement.largeSpacing(): Arrangement.HorizontalOrVertical =
    spacedBy(dimensionResource(com.upsaclay.common.R.dimen.large_padding))