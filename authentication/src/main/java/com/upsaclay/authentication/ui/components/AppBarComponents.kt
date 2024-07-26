package com.upsaclay.authentication.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.upsaclay.authentication.R
import com.upsaclay.core.ui.theme.GedoiseTheme
import com.upsaclay.core.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RegistrationTopBar(
    navController: NavController,
    currentStep: Int,
    maxStep: Int
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.registration),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    painter = painterResource(id = com.upsaclay.core.R.drawable.ic_partial_arrow_left),
                    contentDescription = null
                )
            }
        },
        actions = {
            Text(
                text = stringResource(id = R.string.step, currentStep, maxStep),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                modifier = Modifier.padding(end = MaterialTheme.spacing.medium)
            )
        }
    )
}

@Preview
@Composable
internal fun RegistrationTopBarPreview() {
    GedoiseTheme {
        RegistrationTopBar(
            navController = rememberNavController(),
            currentStep = 1,
            maxStep = 3
        )
    }
}