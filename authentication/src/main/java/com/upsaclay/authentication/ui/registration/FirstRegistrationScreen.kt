package com.upsaclay.authentication.ui.registration

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.upsaclay.authentication.R
import com.upsaclay.authentication.ui.components.RegistrationTopBar
import com.upsaclay.core.data.model.Screen
import com.upsaclay.core.ui.components.MyDropDownMenu
import com.upsaclay.core.ui.components.PrimaryLargeButton
import com.upsaclay.core.ui.theme.GedoiseTheme
import com.upsaclay.core.ui.theme.spacing
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel


@Composable
fun FirstRegistrationScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel = koinViewModel()
) {

    var selectedItem by remember {
        mutableStateOf(registrationViewModel.currentSchoolLevel)
    }
    var expanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        registrationViewModel.resetEmail()
        registrationViewModel.resetPassword()
    }

    Scaffold(
        topBar = {
            RegistrationTopBar(
                navController = navController,
                currentStep = 1,
                maxStep = MAX_STEP
            )
        }
    ) {
        Box(
            Modifier
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = MaterialTheme.spacing.medium,
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium
                )
                .fillMaxSize(),
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

                MyDropDownMenu(
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

            PrimaryLargeButton(
                text = stringResource(id = com.upsaclay.core.R.string.next),
                onClick = {
                    registrationViewModel.updateSchoolLevel(selectedItem)
                    navController.navigate(Screen.SECOND_REGISTRATION_SCREEN.route)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        }
    }
}

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
        Scaffold(
            topBar = {
                RegistrationTopBar(
                    navController = rememberNavController(),
                    currentStep = 1,
                    maxStep = MAX_STEP
                )
            }
        ) {
            Box(
                Modifier
                    .padding(
                        top = it.calculateTopPadding(),
                        bottom = MaterialTheme.spacing.medium,
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium
                    )
                    .fillMaxSize(),
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
                    MyDropDownMenu(
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

                PrimaryLargeButton(
                    text = stringResource(id = com.upsaclay.core.R.string.next),
                    onClick = {},
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                )
            }
        }
    }
}