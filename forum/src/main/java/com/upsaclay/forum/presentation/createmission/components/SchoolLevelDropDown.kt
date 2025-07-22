package com.upsaclay.forum.presentation.createmission.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.presentation.components.MultiSelectionDropDownMenu
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.forum.R

@Composable
fun SchoolLevelDropDownMenu(
    modifier: Modifier = Modifier,
    schoolLevels: List<SchoolLevel>,
    selectedSchoolLevels: List<SchoolLevel>,
    onSelectedSchoolLevelsChange: (SchoolLevel) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val value = when  {
        selectedSchoolLevels.isEmpty() -> stringResource(R.string.everyone)
        selectedSchoolLevels.size == schoolLevels.size -> stringResource(R.string.everyone)
        else -> selectedSchoolLevels.joinToString(" - ")
    }

    MultiSelectionDropDownMenu(
        modifier = modifier.fillMaxWidth(),
        items = schoolLevels.map { it.toString() },
        selectedItems = selectedSchoolLevels.map { it.toString() },
        value = value,
        label = stringResource(R.string.school_level),
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_fill_school),
                contentDescription = null,
            )
        },
        onItemClicked = { SchoolLevel.fromString(it)?.let(onSelectedSchoolLevelsChange) },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        onDismissRequest = { expanded = false }
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun SchoolLevelDropDownPreview() {
    var selectedSchoolLevels by remember { mutableStateOf(emptyList<SchoolLevel>()) }
    GedoiseTheme {
        Surface {
            SchoolLevelDropDownMenu(
                modifier = Modifier.padding(MaterialTheme.spacing.small),
                schoolLevels = SchoolLevel.entries,
                selectedSchoolLevels = emptyList(),
                onSelectedSchoolLevelsChange = {
                    selectedSchoolLevels = if (selectedSchoolLevels.contains(it)) {
                        selectedSchoolLevels - it
                    } else {
                        selectedSchoolLevels + it
                    }
                }
            )
        }
    }
}
