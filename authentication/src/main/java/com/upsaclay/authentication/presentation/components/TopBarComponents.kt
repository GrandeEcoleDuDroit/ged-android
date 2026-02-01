package com.upsaclay.authentication.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.upsaclay.authentication.R
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RegistrationScaffold(
    onBackClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = stringResource(id = R.string.registration)
            )
        },
        content = content
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun RegistrationScaffoldPreview() {
    GedoiseTheme {
        RegistrationScaffold(onBackClick = {}) {}
    }
}