package com.upsaclay.authentication.presentation.registration

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.upsaclay.authentication.R
import com.upsaclay.authentication.domain.model.RegistrationState
import com.upsaclay.authentication.presentation.components.LargeButton
import com.upsaclay.authentication.presentation.components.RegistrationTopBar
import com.upsaclay.common.domain.model.Screen
import com.upsaclay.common.presentation.components.ErrorText
import com.upsaclay.common.presentation.components.OverlayLoadingScreen
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import org.koin.androidx.compose.koinViewModel

@Composable
fun ThirdRegistrationScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel = koinViewModel()
) {
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            registrationViewModel.updateProfilePictureUri(uri)
        }
    )
    val registrationState by registrationViewModel.registrationState.collectAsState()

    if (registrationState == RegistrationState.REGISTERED) {
        navController.navigate(Screen.NEWS.route) {
            popUpTo(Screen.NEWS.route) { inclusive = true }
        }
    }

    RegistrationTopBar(
        navController = navController,
        currentStep = 3,
        maxStep = MAX_REGISTRATION_STEP
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.add_profile_picture),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(MaterialTheme.spacing.medium))

            ProfilePicture(
                imageUri = registrationViewModel.profilePictureUri,
                scaleImage = 2f,
                onClick = {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )

            registrationViewModel.profilePictureUri?.let {
                TextButton(
                    onClick = { registrationViewModel.resetProfilePictureUri() }
                ) {
                    Text(
                        text = stringResource(id = com.upsaclay.common.R.string.remove),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Text(
                text = registrationViewModel.fullName,
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = registrationViewModel.email,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

            if (registrationState == RegistrationState.REGISTRATION_ERROR) {
                ErrorText(text = stringResource(id = R.string.error_registration))
            }
        }

        LargeButton(
            text = stringResource(id = com.upsaclay.common.R.string.finish),
            onClick = { registrationViewModel.register() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )

        if (registrationState == RegistrationState.LOADING) {
            OverlayLoadingScreen()
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview
@Composable
fun ThirdRegistrationScreenPreview() {
    val errorRegistration = false
    val pictureChanged = false
    val isLoading = false

    GedoiseTheme {
        RegistrationTopBar(
            navController = rememberNavController(),
            currentStep = 3,
            maxStep = MAX_REGISTRATION_STEP
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.add_profile_picture),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(MaterialTheme.spacing.medium))

                Box(
                    modifier = Modifier.size(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = com.upsaclay.common.R.drawable.default_profile_picture),
                        contentDescription = "",
                        modifier = Modifier
                            .size(100.dp)
                            .scale(2f)
                    )
                    ProfilePicture(
                        imageUrl = null,
                        scaleImage = 2f,
                        onClick = {}
                    )
                }

                if (pictureChanged) {
                    TextButton(
                        onClick = { }
                    ) {
                        Text(
                            text = stringResource(id = com.upsaclay.common.R.string.remove),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Text(
                    text = "Pierre Dupont",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "pierre.dupont@universite-paris-saclay.fr",
                    maxLines = 1,
                    color = Color.DarkGray,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                if (errorRegistration) {
                    Text(
                        text = stringResource(id = R.string.error_registration),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            LargeButton(
                text = stringResource(id = com.upsaclay.common.R.string.finish),
                onClick = { },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        }
        if (isLoading) {
            OverlayLoadingScreen()
        }
    }
}