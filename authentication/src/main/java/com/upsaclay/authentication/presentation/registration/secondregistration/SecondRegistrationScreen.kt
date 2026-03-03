package com.upsaclay.authentication.presentation.registration.secondregistration

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.authentication.R
import com.upsaclay.authentication.presentation.components.RegistrationScaffold
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.extension.rootMediumPadding
import com.upsaclay.common.presentation.components.PrimaryButton
import com.upsaclay.common.presentation.components.SingleSelectionDropDown
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import org.koin.androidx.compose.koinViewModel

@Composable
fun SecondRegistrationDestination(
    onNextClick: (SchoolLevel) -> Unit,
    onBackClick: () -> Unit,
    viewModel: SecondRegistrationViewModel = koinViewModel()
) {
    val schoolLevel by viewModel.schoolLevel.collectAsState()

    SecondRegistrationScreen(
        schoolLevel = schoolLevel,
        schoolLevels = viewModel.schoolLevels,
        onItemClick = viewModel::onSchoolLevelChange,
        onNextClick = { onNextClick(schoolLevel) },
        onBackClick = onBackClick
    )
}

@Composable
private fun SecondRegistrationScreen(
    schoolLevel: SchoolLevel,
    schoolLevels: List<SchoolLevel>,
    onItemClick: (SchoolLevel) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    RegistrationScaffold(
        onBackClick = onBackClick
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .rootMediumPadding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures(onPress = { expanded = false })
                }
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
            ) {
                Text(
                    text = stringResource(id = R.string.select_level_school),
                    style = MaterialTheme.typography.titleMedium
                )

                SingleSelectionDropDown(
                    items = schoolLevels.map { it.value },
                    selectedItem = schoolLevel.value,
                    onItemClicked = {
                        onItemClick(SchoolLevel.fromValue(it))
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            PrimaryButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .testTag(stringResource(id = R.string.registration_screen_next_button_tag)),
                text = stringResource(id = com.upsaclay.common.R.string.next),
                onClick = onNextClick
            )
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun SecondRegistrationScreenPreview() {
    var selectedItem by remember { mutableStateOf(SchoolLevel.LEVEL_1) }

    GedoiseTheme {
        SecondRegistrationScreen(
            schoolLevel = selectedItem,
            schoolLevels = SchoolLevel.all,
            onItemClick = { selectedItem = it },
            onNextClick = {},
            onBackClick = {}
        )
    }
}