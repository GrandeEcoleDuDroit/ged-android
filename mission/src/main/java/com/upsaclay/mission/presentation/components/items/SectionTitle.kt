package com.upsaclay.mission.presentation.components.items

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews

@Composable
fun SectionTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.titleMedium
    )
}

@PhonePreviews
@Composable
private fun ParticipantItemPreview() {
    GedoiseTheme {
        Surface {
            SectionTitle(
                title = "Section title"
            )
        }
    }
}