package com.upsaclay.authentication.presentation.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.upsaclay.authentication.R
import com.upsaclay.authentication.presentation.components.LargeButton
import com.upsaclay.authentication.presentation.components.RegistrationTopBar
import com.upsaclay.common.domain.model.Screen
import com.upsaclay.common.presentation.components.SimpleDropDownMenu
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun FirstRegistrationScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel = koinViewModel()
) {
    var selectedItem by remember {
        mutableStateOf(registrationViewModel.schoolLevel)
    }
    var expanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        registrationViewModel.resetEmail()
        registrationViewModel.resetPassword()
    }

    RegistrationTopBar(
        navController = navController,
        currentStep = 1,
        maxStep = MAX_REGISTRATION_STEP
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.select_level_school),
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            SimpleDropDownMenu(
                items = registrationViewModel.schoolLevels,
                selectedItem = selectedItem,
                onItemClicked = { item ->
                    selectedItem = item
                    expanded = false
                },
                expanded = expanded,
                onExpandedChange = { isExpanded ->
                    expanded = isExpanded
                },
                onDismissRequest = {
                    expanded = false
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        LargeButton(
            text = stringResource(id = com.upsaclay.common.R.string.next),
            onClick = {
                registrationViewModel.updateSchoolLevel(selectedItem)
                navController.navigate(com.upsaclay.common.domain.model.Screen.SECOND_REGISTRATION_SCREEN.route)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview
@Composable
private fun FirstRegistrationScreenPreview() {
    val items = persistentListOf("GED 1", "GED 2", "GED 3")
    var selectedItem by remember {
        mutableStateOf(items[0])
    }
    var expanded by remember {
        mutableStateOf(false)
    }

    GedoiseTheme {
        RegistrationTopBar(
            navController = NavController(LocalContext.current),
            currentStep = 1,
            maxStep = MAX_REGISTRATION_STEP
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.select_level_school),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                SimpleDropDownMenu(
                    items = items,
                    selectedItem = selectedItem,
                    onItemClicked = { item ->
                        selectedItem = item
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { isExpanded ->
                        expanded = isExpanded
                    },
                    onDismissRequest = {
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            LargeButton(
                text = stringResource(id = com.upsaclay.common.R.string.next),
                onClick = {},
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        }
    }
}
