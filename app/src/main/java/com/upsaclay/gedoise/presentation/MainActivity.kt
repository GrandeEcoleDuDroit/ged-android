package com.upsaclay.gedoise.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.gedoise.presentation.navigation.GedNavHost
import com.upsaclay.gedoise.presentation.navigation.NavigationViewModel
import com.upsaclay.gedoise.presentation.navigation.SplashRoute
import com.upsaclay.gedoise.presentation.notification.NotificationMediator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("MissingPermission")
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModel()
    private val navigationViewModel: NavigationViewModel by viewModel()
    private val notificationMediator: NotificationMediator by inject()
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                notificationMediator.createNotificationChannels()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.updateDataOnAuthChange()
        setupNotificationChannels()

        val splashscreen = installSplashScreen()
        splashscreen.setKeepOnScreenCondition {
            navigationViewModel.uiState.value.startDestination is SplashRoute
        }

        setContent {
            GedoiseTheme {
                GedNavHost(navigationViewModel = navigationViewModel)
            }
        }

        intent?.extras?.let(notificationMediator::onNotificationClick)
    }

    private fun setupNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notificationMediator.createNotificationChannels()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            notificationMediator.createNotificationChannels()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.extras?.let(notificationMediator::onNotificationClick)
    }
}