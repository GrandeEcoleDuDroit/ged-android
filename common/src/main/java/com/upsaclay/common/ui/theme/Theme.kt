package com.upsaclay.common.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.upsaclay.common.ui.theme.GedoiseColor.InputBackground
import com.upsaclay.common.ui.theme.GedoiseColor.Primary
import com.upsaclay.common.ui.theme.GedoiseColor.PrimaryVariant
import com.upsaclay.common.ui.theme.GedoiseColor.Red
import com.upsaclay.common.ui.theme.GedoiseColor.Secondary
import com.upsaclay.common.ui.theme.GedoiseColor.Tertiary
import com.upsaclay.common.ui.theme.GedoiseColor.White


private val LightColorScheme = lightColorScheme (
    primary = Primary,
    secondary = Secondary,
    tertiary = Tertiary,
    primaryContainer = Tertiary,
    background = White,
    error = Red,
    surface = White,
    surfaceVariant = InputBackground,
    secondaryContainer = PrimaryVariant,
    surfaceContainerHigh = PrimaryVariant,
    outlineVariant = Color.LightGray
)

private val DarkColorScheme = darkColorScheme()

@Composable
fun GedoiseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val colors = when {
        dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colorScheme = colors,
            typography = Typography,
            content = content
        )
    }
}