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
import com.google.gson.Gson
import com.upsaclay.common.domain.entity.fcm.FcmDataType
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.gedoise.presentation.navigation.GedNavHost
import com.upsaclay.gedoise.presentation.navigation.NavigationViewModel
import com.upsaclay.gedoise.presentation.navigation.SplashRoute
import com.upsaclay.message.data.mapper.toMessageNotification
import com.upsaclay.message.data.remote.RemoteMessageNotification
import com.upsaclay.message.domain.converter.ConversationJsonParser
import com.upsaclay.message.notification.CONVERSATION_ID_EXTRA
import com.upsaclay.message.notification.MessageNotificationManager
import com.upsaclay.message.presentation.chat.ChatRoute
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("MissingPermission")
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModel()
    private val navigationViewModel: NavigationViewModel by viewModel()
    private val messageNotificationManager: MessageNotificationManager by inject<MessageNotificationManager>()
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                messageNotificationManager.start()
            }
        }
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.listenAuthenticationChanges()
        startNotification()

        val splashscreen = installSplashScreen()
        splashscreen.setKeepOnScreenCondition {
            navigationViewModel.uiState.value.startDestination is SplashRoute
        }

        setContent {
            GedoiseTheme {
                GedNavHost(navigationViewModel = navigationViewModel)
            }
        }

        intent?.let {
            handleIntent(it)
        }
    }

    private fun startNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                messageNotificationManager.start()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            messageNotificationManager.start()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val conversationJson = intent.getStringExtra(CONVERSATION_ID_EXTRA)
        val notificationType = intent.getStringExtra("type")

        when {
            conversationJson != null ->
                navigationViewModel.intentToNavigate(ChatRoute(conversationJson))

            notificationType != null ->
                handleNotificationIntent(notificationType, intent.extras)
        }
    }

    private fun handleNotificationIntent(type: String, extras: Bundle?) {
        when (type) {
            FcmDataType.MESSAGE.toString() -> {
                extras?.getString("value")?.let { value ->
                    val messageNotification = gson.fromJson(value, RemoteMessageNotification::class.java).toMessageNotification()
                    val conversationJson = ConversationJsonParser.toJson(messageNotification.conversation)
                    navigationViewModel.intentToNavigate(ChatRoute(conversationJson))
                }
            }

            else -> Unit
        }
    }
}