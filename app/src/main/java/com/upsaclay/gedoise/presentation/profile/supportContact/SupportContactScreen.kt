package com.upsaclay.gedoise.presentation.profile.supportContact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import com.upsaclay.common.R as commonR
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.SimpleOutlinedTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.gedoise.R

@Composable
fun SupportContactDestination(
    onBackClick: () -> Unit)
: Unit {
    SupportContactScreen(onBackClick = onBackClick)
    
}

@Composable
fun SupportContactScreen(
    onBackClick : () -> Unit
){
    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = stringResource(R.string.support)
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                SimpleOutlinedTextField(
                    value = stringResource(commonR.string.email),
                    label = stringResource(commonR.string.email),
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                SimpleOutlinedTextField(
                    value = stringResource(R.string.message),
                    label = stringResource(R.string.message),
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxHeight(0.90f)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
fun SupportContactScreenPreview(): Unit {
    GedoiseTheme {
        SupportContactScreen(
            onBackClick = {}
        )
    }
}