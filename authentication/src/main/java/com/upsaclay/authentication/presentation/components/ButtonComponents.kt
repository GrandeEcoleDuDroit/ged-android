package com.upsaclay.authentication.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.authentication.R
import com.upsaclay.common.presentation.components.LoadingButton
import com.upsaclay.common.presentation.components.PrimaryButton
import com.upsaclay.common.presentation.theme.GedoiseTheme

@Composable
fun LoginButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    if (isLoading) {
        LoadingButton(modifier = modifier)
    } else {
        PrimaryButton(
            modifier = modifier,
            text = stringResource(id = R.string.login),
            onClick = onClick
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginButtonPreview() {
    var isLoading by remember { mutableStateOf(false) }

    GedoiseTheme {
        LoginButton(
            modifier = Modifier.fillMaxWidth(),
            isLoading = isLoading,
            onClick = { isLoading = !isLoading }
        )
    }
}