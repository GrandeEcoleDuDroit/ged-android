package com.upsaclay.gedoise.presentation.profile.accountinformation

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.rootMediumPadding
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.DefaultDialog
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.user.UserInformationItems
import com.upsaclay.common.utils.Phones
import com.upsaclay.gedoise.R
import com.upsaclay.gedoise.presentation.components.AccountModelBottomSheet
import com.upsaclay.gedoise.presentation.components.AccountTopBar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AccountInformationDestination(
    onBackClick: () -> Unit,
    viewModel: AccountInformationViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackBar = { message: String ->
        scope.launch {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    BackHandler(enabled = uiState.screenState == AccountInformationScreenState.EDIT) {
        viewModel.cancelEdit()
    }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is SingleUiEvent.Error -> showSnackBar(context.getString(event.messageId))

                is SingleUiEvent.Success -> showSnackBar(context.getString(event.messageId))
            }
        }
    }

    if (uiState.user != null) {
        AccountInformationScreen(
            user = uiState.user!!,
            loading = uiState.loading,
            screenState = uiState.screenState,
            profilePictureUri = uiState.profilePictureUri,
            snackbarHostState = snackbarHostState,
            onProfilePictureUriChange = viewModel::onProfilePictureUriChange,
            onScreenStateChange = viewModel::onScreenStateChange,
            onDeleteProfilePictureClick = viewModel::deleteProfilePicture,
            onSaveProfilePictureClick = viewModel::updateProfilePicture,
            onCancelUpdateProfilePictureClick = viewModel::cancelEdit,
            onBackClick = onBackClick
        )
    }
}

@Composable
fun AccountInformationScreen(
    user: User,
    loading: Boolean,
    screenState: AccountInformationScreenState,
    profilePictureUri: Uri?,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onProfilePictureUriChange: (Uri?) -> Unit,
    onScreenStateChange: (AccountInformationScreenState) -> Unit,
    onDeleteProfilePictureClick: () -> Unit,
    onSaveProfilePictureClick: () -> Unit,
    onCancelUpdateProfilePictureClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDeleteProfilePictureDialog by remember { mutableStateOf(false) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                onProfilePictureUriChange(it)
                onScreenStateChange(AccountInformationScreenState.EDIT)
            }
        }
    )

    if (showDeleteProfilePictureDialog) {
        DefaultDialog(
            modifier = Modifier
                .testTag(stringResource(id = R.string.account_screen_delete_profile_picture_dialog_tag)),
            text = stringResource(id = R.string.delete_profile_picture_dialog_message),
            confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
            critical = true,
            onConfirm = {
                showDeleteProfilePictureDialog = false
                onDeleteProfilePictureClick()
            },
            onCancel = { showDeleteProfilePictureDialog = false }
        )
    }

    if (loading) {
        LoadingDialog()
    }

    Scaffold(
        topBar = {
            AccountTopBar(
                isEdited = screenState == AccountInformationScreenState.EDIT,
                onSaveClick = onSaveProfilePictureClick,
                onCancelClick = onCancelUpdateProfilePictureClick,
                onBackClick = onBackClick
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    modifier = Modifier.testTag(stringResource(id = R.string.account_screen_snackbar_tag))
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .rootMediumPadding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
            AccountImage(
                modifier = Modifier.testTag(stringResource(id = R.string.account_screen_profile_picture_tag)),
                isEdited = screenState == AccountInformationScreenState.EDIT,
                profilePictureUri = profilePictureUri,
                profilePictureUrl = user.profilePictureUrl,
                onClick = {
                    if (screenState == AccountInformationScreenState.EDIT) {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    } else {
                        showBottomSheet = true
                    }
                }
            )

            SelectionContainer {
                UserInformationItems(user = user)
            }
        }

        if (showBottomSheet) {
            AccountModelBottomSheet(
                onDismiss = { showBottomSheet = false },
                onNewProfilePictureClick = {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                showDeleteProfilePicture = user.profilePictureUrl != null,
                onDeleteProfilePictureClick = {
                    showDeleteProfilePictureDialog = true
                }
            )
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun AccountScreenPreview() {
    GedoiseTheme {
        Surface {
            AccountInformationScreen(
                user = userFixture,
                loading = false,
                screenState = AccountInformationScreenState.READ,
                profilePictureUri = null,
                snackbarHostState = SnackbarHostState(),
                onProfilePictureUriChange = {},
                onScreenStateChange = {},
                onDeleteProfilePictureClick = {},
                onSaveProfilePictureClick = {},
                onCancelUpdateProfilePictureClick = {},
                onBackClick = {}
            )
        }
    }
}