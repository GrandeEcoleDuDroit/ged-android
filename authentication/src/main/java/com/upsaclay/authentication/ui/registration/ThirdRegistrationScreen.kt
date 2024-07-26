package com.upsaclay.authentication.ui.registration

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.upsaclay.authentication.R
import com.upsaclay.authentication.data.model.RegistrationState
import com.upsaclay.authentication.ui.components.RegistrationTopBar
import com.upsaclay.core.data.model.Screen
import com.upsaclay.core.ui.components.InfiniteCircularProgressIndicator
import com.upsaclay.core.ui.components.PrimaryLargeButton
import com.upsaclay.core.ui.theme.GedoiseTheme
import com.upsaclay.core.ui.theme.spacing
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
        navController.navigate(Screen.HOME.route)
    }

    if(registrationState == RegistrationState.RECOGNIZED_ACCOUNT) {
        registrationViewModel.resetRegistrationState()
        Log.d("RegistrationViewModel", registrationState.toString())
    }

    if (registrationViewModel.profilePictureUri == null) {
        registrationViewModel.resetProfilePictureUri()
    }

    Scaffold(
        topBar = {
            RegistrationTopBar(
                navController = navController,
                currentStep = 3,
                maxStep = MAX_STEP
            )
        }
    ) {
        Box(
            Modifier
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = MaterialTheme.spacing.medium,
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium
                )
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.add_profile_picture),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(Modifier.height(MaterialTheme.spacing.medium))
                AsyncImage(
                    model = registrationViewModel.profilePictureUri,
                    contentDescription = stringResource(id = com.upsaclay.core.R.string.profile_picture_description),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .fillMaxWidth(0.6f)
                        .fillMaxHeight(0.3f)
                        .clickable {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                Text(
                    text = registrationViewModel.fullName,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = registrationViewModel.email,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

                if (registrationState == RegistrationState.ERROR_REGISTRATION) {
                    Text(
                        text = stringResource(id = R.string.error_registration),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                if (registrationState == RegistrationState.LOADING) {
                    InfiniteCircularProgressIndicator()
                }
            }


            PrimaryLargeButton(
                text = stringResource(id = com.upsaclay.core.R.string.finish),
                onClick = {
                    registrationViewModel.register()
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun FourthRegistrationStepPreview() {
    val errorRegistration = true
    GedoiseTheme {
        Scaffold(
            topBar = {
                RegistrationTopBar(
                    navController = rememberNavController(),
                    currentStep = 3,
                    maxStep = MAX_STEP
                )
            }
        ) {
            Box(
                Modifier
                    .padding(
                        top = it.calculateTopPadding(),
                        bottom = MaterialTheme.spacing.medium,
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium
                    )
                    .fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(id = R.string.add_profile_picture),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(Modifier.height(MaterialTheme.spacing.medium))

                    Image(
                        painter = painterResource(id = com.upsaclay.core.R.drawable.default_profile_picture),
                        contentDescription = stringResource(id = com.upsaclay.core.R.string.profile_picture_description),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .fillMaxWidth(0.5f)
                            .clickable {}
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    Text(
                        text = "Pierre Dupont",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Text(
                        text = "pierre.dupont@universite-paris-saclay.fr",
                        maxLines = 1,
                        color = Color.DarkGray,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))

                    if (errorRegistration) {
                        Text(
                            text = stringResource(id = R.string.error_registration),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                    if (!errorRegistration)
                        InfiniteCircularProgressIndicator()
                }

                PrimaryLargeButton(
                    text = stringResource(id = com.upsaclay.core.R.string.finish),
                    onClick = { },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                )
            }
        }
    }
}

