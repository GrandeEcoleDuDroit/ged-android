package com.upsaclay.news.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.upsaclay.common.data.model.MenuItemData
import com.upsaclay.common.data.model.Screen
import com.upsaclay.common.ui.components.ClickableMenuItem
import com.upsaclay.common.ui.components.PullToRefreshComponent
import com.upsaclay.common.ui.theme.GedoiseColor
import com.upsaclay.common.ui.theme.GedoiseTheme
import com.upsaclay.common.ui.theme.spacing
import com.upsaclay.news.R
import com.upsaclay.news.announcementItemsFixture
import com.upsaclay.news.domain.model.Announcement
import com.upsaclay.news.domain.model.AnnouncementState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

private const val URL_BLOGSPOT = "https://grandeecoledudroit.blogspot.com/"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableNewsScreen(
    newsViewModel: NewsViewModel = koinViewModel(),
    navController: NavController
) {
    val announcements = newsViewModel.announcements.collectAsState(emptyList()).value
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var currentAnnouncementSelected by remember { mutableStateOf<Announcement?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val state = newsViewModel.announcementState.collectAsState(initial = AnnouncementState.DEFAULT).value

    val hideBottomSheet: () -> Unit = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                showBottomSheet = false
            }
        }
    }

    val menuItemData: List<MenuItemData> = listOf(
        MenuItemData(
            text = { Text(text = stringResource(id = R.string.edit_announcement)) },
            icon = { Icon(imageVector = Icons.Default.Edit, contentDescription = null) },
            onClick = {
                hideBottomSheet()
            }
        ),
        MenuItemData(
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

    LaunchedEffect(Unit) {
        newsViewModel.refreshAnnouncements()
    }

    PullToRefreshComponent(
        onRefresh = { newsViewModel.refreshAnnouncements() },
        refreshing = state == AnnouncementState.LOADING,
    ) {
        Column {
            RecentAnnouncementSection(
                announcements = announcements,
                onClickAnnouncement = { announcement ->
                    newsViewModel.updateDisplayedAnnouncement(announcement)
                    navController.navigate(Screen.READ_ANNOUNCEMENT.route)
                },
                onClickEditAnnouncement = { announcement ->
                    showBottomSheet = true
                    currentAnnouncementSelected = announcement
                }
            )

            PostSection()

            if (showBottomSheet) {
                EditAnnouncementModelBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                    menuItemData = menuItemData
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .fillMaxSize()
        ) {
            ExtendedFloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = { navController.navigate(Screen.CREATE_ANNOUNCEMENT.route) },
                icon = {
                    Icon(
                        Icons.Filled.Edit,
                        stringResource(id = R.string.new_announcement)
                    )
                },
                text = { Text(text = stringResource(id = R.string.new_announcement)) },
            )
        }

        if (showDialog) {
            DeleteAnnouncementDialog(
                onDismissRequest = { showDialog = false },
                onConfirmClick = {
                    showDialog = false
                    newsViewModel.deleteAnnouncement(currentAnnouncementSelected!!)
                }
            )
        }
    }
}

@Composable
private fun RecentAnnouncementSection(
    announcements: List<Announcement>,
    onClickAnnouncement: (Announcement) -> Unit,
    onClickEditAnnouncement: (Announcement) -> Unit,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Column(modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium)) {
        Text(
            text = stringResource(id = R.string.recent_announcements),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        LazyColumn(Modifier.heightIn(max = screenHeight * 0.3f)) {
            if (announcements.isEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.no_announcements),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(announcements) { announcement ->
                    EditableShortAnnouncementItem(
                        announcement = announcement,
                        onClick = { onClickAnnouncement(announcement) },
                        onEditClick = { onClickEditAnnouncement(announcement) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditAnnouncementModelBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    menuItemData: List<MenuItemData>
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
                onClick = menuItemData.onClick!!
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    }
}

@Composable
private fun PostSection() {
    Column {
        Text(
            text = stringResource(id = R.string.news_ged),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
        )

        //TODO : Implémenter la récupération de posts
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
                text = stringResource(id = R.string.delete_announcement_dialog_content),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    )
}

@Preview(showBackground = true ,widthDp = 360, heightDp = 640)
@Composable
fun EditableNewsScreenPreview(){
    GedoiseTheme {
        PullToRefreshComponent(
            onRefresh = { }
        ) {
            Column {
                RecentAnnouncementSectionPreview()

                PostSection()
            }

            Box(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.medium)
                    .fillMaxSize()
            ) {
                ExtendedFloatingActionButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onClick = { },
                    icon = {
                        Icon(
                            Icons.Filled.Edit,
                            stringResource(id = R.string.new_announcement)
                        )
                    },
                    text = { Text(text = stringResource(id = R.string.new_announcement)) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecentAnnouncementSectionPreview(){
    GedoiseTheme {
        Column {
            RecentAnnouncementSection(
                announcements = announcementItemsFixture,
                onClickAnnouncement = {},
                onClickEditAnnouncement = {}
            )
        }
    }
}

@Preview
@Composable
private fun DeleteAnnouncementDialogPreview(){
    GedoiseTheme {
        DeleteAnnouncementDialog(
            onDismissRequest = {},
            onConfirmClick = {}
        )
    }
}