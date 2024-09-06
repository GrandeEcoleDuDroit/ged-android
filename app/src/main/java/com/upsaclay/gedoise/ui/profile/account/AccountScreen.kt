package com.upsaclay.gedoise.ui.profile.account

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.Coil
import com.upsaclay.common.ui.components.ClickableMenuItem
import com.upsaclay.common.ui.components.OverlayLoadingScreen
import com.upsaclay.common.ui.components.ProfilePicture
import com.upsaclay.common.ui.components.ProfilePictureWithIcon
import com.upsaclay.common.ui.components.SimpleDialog
import com.upsaclay.common.ui.theme.GedoiseColor
import com.upsaclay.common.ui.theme.GedoiseTheme
import com.upsaclay.common.ui.theme.spacing
import com.upsaclay.common.utils.userFixture
import com.upsaclay.gedoise.R
import com.upsaclay.gedoise.data.profile.AccountInfo
import com.upsaclay.gedoise.data.profile.AccountScreenState
import com.upsaclay.gedoise.ui.SmallTopBarBack
import com.upsaclay.gedoise.ui.SmallTopBarEdit
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavController,
    accountViewModel: AccountViewModel = koinViewModel()
) {
    val user = accountViewModel.user.collectAsState(initial = null).value
    val accountScreenState = accountViewModel.accountScreenState.collectAsState().value
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDeleteProfilePictureDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val hideBottomSheet: () -> Unit = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                showBottomSheet = false
            }
        }
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                accountViewModel.updateProfilePictureUri(it)
                accountViewModel.updateAccountScreenState(AccountScreenState.EDIT)
            }
        }
    )

    user?.let {
        val accountInfos: ImmutableList<AccountInfo> = persistentListOf(
            AccountInfo(
                stringResource(id = com.upsaclay.common.R.string.last_name),
                user.lastName
            ),
            AccountInfo(
                stringResource(id = com.upsaclay.common.R.string.first_name),
                user.firstName
            ),
            AccountInfo(
                stringResource(id = com.upsaclay.common.R.string.email),
                user.email
            ),
            AccountInfo(
                stringResource(id = com.upsaclay.common.R.string.school_level),
                user.schoolLevel
            )
        )

        Scaffold(
            topBar = { AccountTopBar(navController, accountViewModel, accountScreenState) }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = it.calculateTopPadding() + MaterialTheme.spacing.small,
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.medium
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfilePictureSection(
                        isEdited = accountScreenState == AccountScreenState.EDIT,
                        profilePictureUri = accountViewModel.profilePictureUri,
                        profilePictureUrl = user.profilePictureUrl,
                        onClick = {
                            if (accountScreenState == AccountScreenState.EDIT) {
                                singlePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            } else {
                                showBottomSheet = true
                            }
                        },
                    )

                    accountInfos.forEach { accountInfo ->
                        AccountInfoItem(
                            modifier = Modifier.fillMaxWidth(),
                            accountInfo = accountInfo
                        )
                    }

                    if (showBottomSheet) {
                        AccountModelBottomSheet(
                            onDismissRequest = { showBottomSheet = false },
                            onClickNewProfilePicture = {
                                hideBottomSheet()
                                singlePhotoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            showDeleteProfilePicture = user.profilePictureUrl != null,
                            onClickDeleteProfilePicture = {
                                hideBottomSheet()
                                showDeleteProfilePictureDialog = true
                            }
                        )
                    }
                }

                when (accountScreenState) {
                    AccountScreenState.LOADING -> {
                        OverlayLoadingScreen()
                    }

                    AccountScreenState.PROFILE_PICTURE_UPDATE_ERROR -> {
                        Toast.makeText(
                            context,
                            R.string.error_uploading_profile_picture,
                            Toast.LENGTH_SHORT
                        ).show()
                        accountViewModel.updateAccountScreenState(AccountScreenState.READ)
                    }

                    AccountScreenState.PROFILE_PICTURE_UPDATED -> {
                        Coil.imageLoader(context).shutdown()
                        Toast.makeText(
                            context,
                            R.string.profile_picture_uploaded,
                            Toast.LENGTH_SHORT
                        ).show()
                        accountViewModel.updateAccountScreenState(AccountScreenState.READ)
                    }

                    else -> {}
                }

                if (showDeleteProfilePictureDialog) {
                    SimpleDialog(
                        title = stringResource(id = R.string.delete_current_profile_picture),
                        text = stringResource(id = R.string.delete_profil_picture_dialog_text),
                        onConfirm = {
                            showDeleteProfilePictureDialog = false
                            accountViewModel.deleteUserProfilePicture()
                        },
                        onCancel = { showDeleteProfilePictureDialog = false },
                        onDismiss = { showDeleteProfilePictureDialog = false },
                        confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfilePictureSection(
    isEdited: Boolean,
    profilePictureUri: Uri?,
    profilePictureUrl: String?,
    onClick: () -> Unit,
) {
    val scaleImage = 1.8f

    profilePictureUri?.let { uri ->
        if (!isEdited) {
            ProfilePicture(
                imageUri = uri,
                scaleImage = scaleImage,
                onClick = onClick
            )
        } else {
            ProfilePictureWithIcon(
                imageUri = uri,
                iconVector = Icons.Default.Edit,
                contentDescription = "",
                scale = scaleImage,
                onClick = onClick
            )
        }
    } ?: run {
        ProfilePictureWithIcon(
            imageUrl = profilePictureUrl,
            iconVector = Icons.Default.Edit,
            contentDescription = "",
            scale = scaleImage,
            onClick = onClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountModelBottomSheet(
    onDismissRequest: () -> Unit,
    onClickNewProfilePicture: () -> Unit,
    showDeleteProfilePicture: Boolean = false,
    onClickDeleteProfilePicture: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        ClickableMenuItem(
            modifier = Modifier.fillMaxWidth(),
            text = { Text(text = stringResource(id = R.string.new_profile_picture)) },
            icon = {
                Icon(
                    painter = painterResource(id = com.upsaclay.common.R.drawable.ic_picture),
                    contentDescription = null
                )
            },
            onClick = onClickNewProfilePicture
        )

        if (showDeleteProfilePicture) {
            ClickableMenuItem(
                modifier = Modifier.fillMaxWidth(),
                text = {
                    Text(
                        text = stringResource(id = R.string.delete_current_profile_picture),
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
                onClick = onClickDeleteProfilePicture
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
    }
}

@Composable
private fun AccountInfoItem(
    modifier: Modifier = Modifier,
    accountInfo: AccountInfo
) {
    Column(
        modifier = modifier.padding(vertical = MaterialTheme.spacing.smallMedium),
    ) {
        Text(
            text = accountInfo.label,
            color = GedoiseColor.DarkGrey,
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = accountInfo.value,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun AccountTopBar(
    navController: NavController,
    accountViewModel: AccountViewModel,
    accountScreenState: AccountScreenState
) {
    if (accountScreenState == AccountScreenState.EDIT) {
        SmallTopBarEdit(
            title = stringResource(id = R.string.account_informations),
            onClickCancel = {
                accountViewModel.resetProfilePictureUri()
                accountViewModel.updateAccountScreenState(AccountScreenState.READ)
            },
            onClickSave = { accountViewModel.updateUserProfilePicture() }
        )
    } else {
        SmallTopBarBack(
            navController = navController,
            title = stringResource(id = R.string.account_informations)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AccountScreenPreview() {
    val scaleImage = 1.8f
    val sizeImage = 100.dp * scaleImage
    val hasPictureChanged = false
    val isEdited = false

    val accountInfos: ImmutableList<AccountInfo> = persistentListOf(
        AccountInfo(
            stringResource(id = com.upsaclay.common.R.string.last_name),
            userFixture.lastName
        ),
        AccountInfo(
            stringResource(id = com.upsaclay.common.R.string.first_name),
            userFixture.firstName
        ),
        AccountInfo(
            stringResource(id = com.upsaclay.common.R.string.email),
            userFixture.email
        ),
        AccountInfo(
            stringResource(id = com.upsaclay.common.R.string.school_level),
            userFixture.schoolLevel
        )
    )

    GedoiseTheme {
        Scaffold(
            topBar = {
                if (isEdited) {
                    TopAppBar(
                        title = { Text(text = stringResource(id = R.string.account_informations)) },
                        navigationIcon = {
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null
                                )
                            }
                        },
                        actions = {
                            TextButton(onClick = {}) {
                                Text(
                                    text = stringResource(id = com.upsaclay.common.R.string.save),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    )
                } else {
                    SmallTopBarBack(
                        navController = rememberNavController(),
                        title = stringResource(id = R.string.account_informations)
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = it.calculateTopPadding() + MaterialTheme.spacing.small,
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.medium
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.size(sizeImage)) {
                    Image(
                        painter = painterResource(id = com.upsaclay.common.R.drawable.default_profile_picture),
                        contentDescription = "",
                        modifier = Modifier
                            .border(1.dp, Color.LightGray, CircleShape)
                            .fillMaxSize()
                    )
                    if (!hasPictureChanged) {
                        ProfilePictureWithIcon(
                            imageUrl = "",
                            contentDescription = "",
                            scale = scaleImage,
                            iconVector = Icons.Default.Edit,
                            onClick = {}
                        )
                    }
                }

                accountInfos.forEach { accountInfo ->
                    AccountInfoItem(
                        modifier = Modifier.fillMaxWidth(),
                        accountInfo = accountInfo
                    )
                }
            }
        }
    }
}
