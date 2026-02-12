package com.upsaclay.news.presentation.news.components

import androidx.compose.foundation.Image
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.news.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsTopBar(
    admin: Boolean,
    expanded: Boolean,
    onOpenMenuButtonClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onCreateAnnouncementClick: () -> Unit,
    onCreatePostClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(com.upsaclay.common.R.string.app_name),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {},
                enabled = false,
                modifier = Modifier.scale(1.2f)
            ) {
                Image(
                    painter = painterResource(id = com.upsaclay.common.R.drawable.ged_logo),
                    contentDescription = stringResource(id = com.upsaclay.common.R.string.ged_logo_description),
                    modifier = Modifier.scale(0.85f)
                )
            }
        },
        actions = {
            if (admin) {
                IconButton(onClick = onOpenMenuButtonClick) {
                    Icon(
                        painter = painterResource(com.upsaclay.common.R.drawable.ic_add),
                        contentDescription = stringResource(id = com.upsaclay.common.R.string.menu_icon_button_description)
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = onDismissMenu
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.new_announcement)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_outline_campaign),
                                contentDescription = null
                            )
                        },
                        onClick = onCreateAnnouncementClick
                    )

                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.new_post)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_outline_post),
                                contentDescription = null
                            )
                        },
                        onClick = onCreatePostClick
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

/*
 =====================================================================
                            Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun NewsTopBarPreview() {
    GedoiseTheme {
        NewsTopBar(
            admin = true,
            expanded = false,
            onOpenMenuButtonClick = {},
            onDismissMenu = {},
            onCreateAnnouncementClick = {},
            onCreatePostClick = {}
        )
    }
}