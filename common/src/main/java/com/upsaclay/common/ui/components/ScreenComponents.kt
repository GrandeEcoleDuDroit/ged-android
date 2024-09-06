package com.upsaclay.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.upsaclay.common.ui.theme.GedoiseColor
import com.upsaclay.common.ui.theme.GedoiseTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OverlayLoadingScreen(){
    val backgroundColor = if(isSystemInDarkTheme()) {
        GedoiseColor.DarkBackground
    } else {
        GedoiseColor.LittleTransparentWhite
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .zIndex(1000f)
            .pointerInteropFilter { true }
    ){
        CircularProgressBar(
            modifier = Modifier.align(Alignment.Center),
            scale = 2.5f
        )
    }
}

@Preview
@Composable
private fun LoadingScreenPreview(){
    GedoiseTheme {
        OverlayLoadingScreen()
    }
}