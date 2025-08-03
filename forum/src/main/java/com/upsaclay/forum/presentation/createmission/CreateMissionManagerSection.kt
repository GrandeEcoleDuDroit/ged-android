package com.upsaclay.forum.presentation.createmission

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.white
import com.upsaclay.common.utils.Phones
import com.upsaclay.forum.R
import com.upsaclay.forum.presentation.components.items.ManagerItem

@Composable
fun CreateMissionManagerSection(
    modifier: Modifier = Modifier,
    managers: List<User>,
    onShowManagerListClick: () -> Unit,
    onRemoveManagerClick: (User) -> Unit
) {
    val imageScale = 0.4f

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.smallSpacing()
    ) {
        Text(
            modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
            text = stringResource(R.string.mission_managers),
            style = MaterialTheme.typography.titleMedium,
        )

        Row(
            modifier = Modifier
                .clickable(onClick = onShowManagerListClick)
                .padding(
                    horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding),
                    vertical = dimensionResource(com.upsaclay.common.R.dimen.small_padding)
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.smallSpacing(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceTint),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_outline_add_person),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.white,
                )
            }

            Text(
                text = stringResource(R.string.add_manager)
            )
        }

        SelectionContainer {
            Column(
                modifier = Modifier
                    .padding(start = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                verticalArrangement = Arrangement.smallSpacing()
            ) {
                managers.forEach {
                    Row {
                        ManagerItem(
                            modifier = Modifier.weight(1f),
                            user = it,
                            imageScale = imageScale
                        )

                        if (managers.size > 1) {
                            IconButton(
                                onClick = { onRemoveManagerClick(it) },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Phones
@Composable
fun CreateMissionManagerSectionPreview() {
    var managers by remember { mutableStateOf(usersFixture) }

    GedoiseTheme {
        Surface {
            CreateMissionManagerSection(
                managers = managers,
                onShowManagerListClick = { managers = managers + userFixture },
                onRemoveManagerClick = { managers = managers - it }
            )
        }
    }
}