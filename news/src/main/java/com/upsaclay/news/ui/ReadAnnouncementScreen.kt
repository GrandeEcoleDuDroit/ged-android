package com.upsaclay.news.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.upsaclay.common.domain.model.ClickableMenuItemData
import com.upsaclay.common.domain.model.ElapsedTime
import com.upsaclay.common.domain.model.Screen
import com.upsaclay.common.domain.usecase.GetElapsedTimeUseCase
import com.upsaclay.common.ui.components.ClickableMenuItem
import com.upsaclay.common.ui.components.OverlayLoadingScreen
import com.upsaclay.common.ui.components.ProfilePicture
import com.upsaclay.common.ui.theme.GedoiseColor
import com.upsaclay.common.ui.theme.GedoiseTheme
import com.upsaclay.common.ui.theme.spacing
import com.upsaclay.news.R
import com.upsaclay.news.announcementFixture
import com.upsaclay.news.domain.model.Announcement
import com.upsaclay.news.domain.model.AnnouncementState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadAnnouncementScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    newsViewModel: NewsViewModel = koinViewModel()
) {
    if(newsViewModel.displayedAnnouncement == null) {
        newsViewModel.updateAnnouncementState(AnnouncementState.ANNOUNCEMENT_DISPLAY_ERROR)
        navController.popBackStack()
    }

    val announcement = newsViewModel.displayedAnnouncement!!
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val user = newsViewModel.user.collectAsState(initial = null).value
    val state by newsViewModel.announcementState.collectAsState(AnnouncementState.DEFAULT)
    val hideBottomSheet: () -> Unit = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                showBottomSheet = false
            }
        }
    }

    val bottomSheetItemData: List<ClickableMenuItemData> = listOf(
        ClickableMenuItemData(
            text = { Text(text = stringResource(id = R.string.edit_announcement)) },
            icon = { Icon(imageVector = Icons.Default.Edit, contentDescription = null) },
            onClick = {
                navController.navigate(Screen.EDIT_ANNOUNCEMENT.route)
                hideBottomSheet()
            }
        ),
        ClickableMenuItemData(
            text = {
                Text(
                    text = stringResource(id = R.string.delete_announcement),
                    color = GedoiseColor.Red
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = GedoiseColor.Red
                )
            },
            onClick = {
                hideBottomSheet()
                showDialog = true
            }
        )
    )

    Box {
        if(state != AnnouncementState.LOADING) {
            user?.let {
                Column(
                    modifier = modifier
                        .padding(
                            start = MaterialTheme.spacing.medium,
                            end = MaterialTheme.spacing.medium,
                            bottom = MaterialTheme.spacing.medium
                        )
                        .verticalScroll(rememberScrollState())
                    ) {

                    if (user.isMember && announcement.author == user) {
                        EditableTopSection(
                            announcement = announcement,
                            onEditClick = { showBottomSheet = true }
                        )
                    } else {
                        ReadOnlyTopSection(announcement)
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    announcement.title?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Spacer(Modifier.height(MaterialTheme.spacing.medium))
                    }

                    Text(text = announcement.content)

                    if (showBottomSheet) {
                        EditAnnouncementModelBottomSheet(
                            onDismissRequest = { showBottomSheet = false },
                            sheetState = sheetState,
                            menuItemData = bottomSheetItemData
                        )
                    }

                    if (showDialog) {
                        DeleteAnnouncementDialog(
                            onDismissRequest = { showDialog = false },
                            onConfirmClick = {
                                showDialog = false
                                newsViewModel.deleteAnnouncement(announcement)
                            }
                        )
                    }
                }
            }
        }
        else {
            OverlayLoadingScreen()
        }
    }
}

@Composable
private fun ReadOnlyTopSection(announcement: Announcement) {
    val context = LocalContext.current
    val elapsedTime = GetElapsedTimeUseCase().fromLocalDateTime(announcement.date)
    val elapsedTimeValue = when(elapsedTime) {
        is ElapsedTime.Now -> stringResource(id = com.upsaclay.common.R.string.now)
        is ElapsedTime.Minute -> context.getString(com.upsaclay.common.R.string.minute_ago, elapsedTime.value)
        is ElapsedTime.Hour -> context.getString(com.upsaclay.common.R.string.hour_ago, elapsedTime.value)
        is ElapsedTime.Day -> context.getString(com.upsaclay.common.R.string.day_ago, elapsedTime.value)
        is ElapsedTime.Week -> context.getString(com.upsaclay.common.R.string.week_ago, elapsedTime.value)
        is ElapsedTime.After -> {
            val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            announcement.date.format(dateFormat)
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        ProfilePicture(
            imageUrl = announcement.author.profilePictureUrl,
            scaleImage = 0.45f
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

        Text(
            text = announcement.author.fullName,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(fill = false, weight = 1f)
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

        Text(
            text = elapsedTimeValue,
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray
        )
    }
}

@Composable
private fun EditableTopSection(
    announcement: Announcement,
    onEditClick: () -> Unit
) {
    val context = LocalContext.current
    val elapsedTime = GetElapsedTimeUseCase().fromLocalDateTime(announcement.date)
    val elapsedTimeValue = when(elapsedTime) {
        is ElapsedTime.Now -> stringResource(id = com.upsaclay.common.R.string.now)
        is ElapsedTime.Minute -> context.getString(com.upsaclay.common.R.string.minute_ago, elapsedTime.value)
        is ElapsedTime.Hour -> context.getString(com.upsaclay.common.R.string.hour_ago, elapsedTime.value)
        is ElapsedTime.Day -> context.getString(com.upsaclay.common.R.string.day_ago, elapsedTime.value)
        is ElapsedTime.Week -> context.getString(com.upsaclay.common.R.string.week_ago, elapsedTime.value)
        is ElapsedTime.After -> {
            val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            announcement.date.format(dateFormat)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfilePicture(
            imageUrl = announcement.author.profilePictureUrl,
            scaleImage = 0.45f
        )

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

            Text(
                text = announcement.author.fullName,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(fill = false, weight = 1f)
            )

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

            Text(
                text = elapsedTimeValue,
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

        IconButton(
            onClick = onEditClick,
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.MoreVert,
                tint = Color.Gray,
                contentDescription = stringResource(id = R.string.announcement_item_more_vert_description)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReadOnlyAnnouncementScreenPreview(){
    GedoiseTheme {
        val announcement = announcementFixture

        Column(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {
            ReadOnlyTopSection(announcement)

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            announcement.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.height(MaterialTheme.spacing.medium))
            }

            Text(text = announcement.content,)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditableAnnouncementScreenPreview(){
    GedoiseTheme {

        val announcement = announcementFixture

        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        ) {

            EditableTopSection(
                announcement = announcement,
                onEditClick = {}
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            announcement.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.height(MaterialTheme.spacing.medium))
            }

            Text(
                text = announcement.content,
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditAnnouncementModelBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    menuItemData: List<ClickableMenuItemData>
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        menuItemData.forEach { menuItemData ->
            ClickableMenuItem(
                modifier = Modifier.fillMaxWidth(),
                text = menuItemData.text,
                icon = menuItemData.icon,
                onClick = menuItemData.onClick
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    }
}

@Composable
private fun DeleteAnnouncementDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text(
                    text = stringResource(id = com.upsaclay.common.R.string.delete),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = com.upsaclay.common.R.string.cancel))
            }
        },
        text = {
            Text(
                text = stringResource(id = R.string.delete_announcement_dialog_text),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    )
}

@Preview
@Composable
private fun DeleteAnnouncementDialogPreview() {
    GedoiseTheme {
        DeleteAnnouncementDialog(
            onDismissRequest = {},
            onConfirmClick = {}
        )
    }
}
