package com.upsaclay.mission.presentation.components.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.common.presentation.components.SectionTitle
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.iconBackground
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.mission.R
import com.upsaclay.mission.presentation.components.RemoveButton
import com.upsaclay.mission.presentation.components.items.MissionUserItem

@Composable
fun MissionFormManagerSection(
    modifier: Modifier = Modifier,
    managers: List<User>,
    onShowManagerListClick: () -> Unit,
    onRemoveManagerClick: (User) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SectionTitle(
            modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
            title = stringResource(R.string.managers)
        )

        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.small_padding)))

        AddManagerItem(
            modifier = Modifier.clickable(onClick = onShowManagerListClick)
        )

        managers.forEach {
            MissionUserItem(
                user = it,
                imageScale = 0.4f,
                trailingContent = if (managers.size > 1) {
                    @Composable {
                        RemoveButton(
                            onClick = { onRemoveManagerClick(it) }
                        )
                    }
                } else null
            )
        }
    }
}

@Composable
private fun AddManagerItem(modifier: Modifier = Modifier) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_outline_add_person),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        headlineContent = {
            Text(
                text = stringResource(R.string.add_manager),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
fun MissionFormManagerSectionPreview() {
    GedoiseTheme {
        Surface {
            MissionFormManagerSection(
                managers = usersFixture,
                onShowManagerListClick = {},
                onRemoveManagerClick = {}
            )
        }
    }
}