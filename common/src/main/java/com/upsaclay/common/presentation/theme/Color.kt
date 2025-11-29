package com.upsaclay.common.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal val white = Color(0xFFFFFFFF)
internal val black = Color(0xFF121212)

internal val primaryLight = Color(0xFF67708A)
internal val primaryContainerLight = Color(0xFFDDE0E7)
internal val onPrimaryContainerLight = Color(0xFF45474D)
internal val onPrimaryLight = white
internal val secondaryLight = Color(0xFF0B86FF)
internal val secondaryContainerLight = Color(0xFFD7DCE5)
internal val tertiaryLight = Color(0xFF009688)
internal val backgroundLight = Color(0xFFFFFFFF)
internal val onBackgroundLight = black
internal val errorLight = Color(0xFFED5245)
internal val surfaceLight = Color(0xFFFFFFFF)
internal val onSurfaceVariantLight = Color(0xFF4F4F4F)
internal val inverseSurfaceLight = Color(0xFF303133)
internal val inverseOnSurfaceLight = Color(0xFFEFF1F4)
internal val onSurfaceLight = black
internal val surfaceContainerHighLight = Color(0xFFE7E9EC)
internal val surfaceVariantLight = Color(0xFFE0E4EC)
internal val outlineLight = Color(0xFF74777E)
internal val outlineVariantLight = Color(0xFFC4C8D0)

internal val primaryDark = Color(0xFF67708A)
internal val primaryContainerDark = Color(0xFF444958)
internal val onPrimaryContainerDark = Color(0xFFE3E3E3)
internal val onPrimaryDark = white
internal val errorDark = Color(0xFFD64A4C)
internal val surfaceDark = Color(0xFF191919)
internal val surfaceContainerHighDark = Color(0xFF444958)
internal val surfaceVariantDark = Color(0xFF45484F)
internal val onSurfaceDark = white
internal val onSurfaceVariantDark = Color(0xFFD2D2D2)
internal val inverseSurfaceDark = white
internal val inverseOnSurfaceDark = Color(0xFF303133)
internal val onSecondaryContainerDark = white
internal val secondaryContainerDark = Color(0xFF444958)
internal val backgroundDark = Color(0xFF191919)
internal val onBackgroundDark = white
internal val outlineDark = Color(0xFF939393)
internal val outlineVariantDark = Color(0xFF3C3C3C)

val ColorScheme.black: Color
    @Composable
    get() = com.upsaclay.common.presentation.theme.black

val ColorScheme.white: Color
    @Composable
    get() = com.upsaclay.common.presentation.theme.white

val ColorScheme.gold: Color
    @Composable
    get() = Color(0xFFB98129)

val ColorScheme.inputBackground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF323232) else Color(0xFFEEEEEE)

val ColorScheme.inputForeground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFBEBEBE) else Color(0xFF646464)

val ColorScheme.previewText: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFA1A4B0) else Color(0xFF6F7181)

val ColorScheme.hintText: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF929298) else Color(0xFF8C8C8C)

val ColorScheme.informationText: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFA1A4B0) else Color(0xFF6F7181)

val ColorScheme.loadingImageBackground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF323232) else Color(0xFFEEEEEE)

val ColorScheme.emptyImageBackground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF323232) else Color(0xFFE6E6E6)

val ColorScheme.emptyImageForeground: Color
    @Composable
    get() = MaterialTheme.colorScheme.onSurfaceVariant

val ColorScheme.defaultImageForeground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFC2C5CF) else Color(0xFFA8ACB5)

val ColorScheme.iconBackground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF323232) else Color(0xFFE6E6E6)

val ColorScheme.listDivider: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF464646) else Color(0xFFBEBEBE)


val ColorScheme.activatedButtonColors: ButtonColors
    @Composable
    get() = ButtonDefaults.buttonColors(
        containerColor = if (isSystemInDarkTheme()) Color(0xFF323232) else Color(0xFFE6E6E6),
        contentColor = if (isSystemInDarkTheme()) white else Color(0xFF3C3C3C)
    )

val ColorScheme.loadingButtonColors: ButtonColors
    @Composable
    get() = ButtonDefaults.buttonColors(
        disabledContainerColor = MaterialTheme.colorScheme.primary,
        disabledContentColor = MaterialTheme.colorScheme.white
    )

val ColorScheme.imageIconButtonColors: IconButtonColors
    @Composable
    get() = IconButtonDefaults.iconButtonColors(
        containerColor = if (isSystemInDarkTheme()) {
            Color(0xFF323232)
        } else {
            Color(0xFFF5F5F5)
        }.copy(alpha = 0.7f)
    )


val ColorScheme.checkBoxColor: CheckboxColors
    @Composable
    get() = CheckboxColors(
        checkedCheckmarkColor = CheckboxDefaults.colors().checkedCheckmarkColor,
        uncheckedCheckmarkColor = CheckboxDefaults.colors().uncheckedCheckmarkColor,
        checkedBoxColor = CheckboxDefaults.colors().checkedBoxColor,
        uncheckedBoxColor = CheckboxDefaults.colors().uncheckedBoxColor,
        disabledCheckedBoxColor = CheckboxDefaults.colors().disabledCheckedBoxColor,
        disabledUncheckedBoxColor = CheckboxDefaults.colors().disabledUncheckedBoxColor,
        disabledIndeterminateBoxColor = CheckboxDefaults.colors().disabledIndeterminateBoxColor,
        checkedBorderColor = CheckboxDefaults.colors().checkedBorderColor,
        uncheckedBorderColor = MaterialTheme.colorScheme.outline,
        disabledBorderColor = CheckboxDefaults.colors().disabledBorderColor,
        disabledUncheckedBorderColor = CheckboxDefaults.colors().disabledUncheckedBorderColor,
        disabledIndeterminateBorderColor = CheckboxDefaults.colors().disabledIndeterminateBorderColor
    )


val ColorScheme.outlinedTextFieldColor: TextFieldColors
    @Composable
    get() = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = Color.Transparent
    )