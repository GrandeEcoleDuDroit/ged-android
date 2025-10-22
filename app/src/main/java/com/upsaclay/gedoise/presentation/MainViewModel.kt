
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.domain.usecase.ListenAuthenticationStateUseCase
import com.upsaclay.app.domain.ClearDataUseCase
import com.upsaclay.app.domain.FcmTokenUseCase
import com.upsaclay.app.domain.ListenBlockedUserEvents
import com.upsaclay.app.domain.ListenRemoteUserUseCase
import com.upsaclay.app.domain.SynchronizeDataUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteConversationsUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val listenAuthenticationStateUseCase: ListenAuthenticationStateUseCase,
    private val listenRemoteConversationsUseCase: ListenRemoteConversationsUseCase,
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase,
    private val listenRemoteUserUseCase: ListenRemoteUserUseCase,
    private val listenBlockedUserEvents: ListenBlockedUserEvents,
    private val synchronizeDataUseCase: SynchronizeDataUseCase,
    private val clearDataUseCase: ClearDataUseCase,
    private val fcmTokenUseCase: FcmTokenUseCase,
): ViewModel() {
    private var listeningJob: Job? = null

    fun updateDataOnAuthChange() {
        viewModelScope.launch {
            listenAuthenticationStateUseCase.authenticated.collectLatest { authenticated ->
                try {
                    if (authenticated) {
                        listenData()
                        synchronizeDataUseCase()
                        fcmTokenUseCase.sendUnsetToken()
                    } else {
                        stopListenData()
                        delay(2000)
                        clearDataUseCase()
                        fcmTokenUseCase.generateNewToken()
                    }
                } catch (e: Exception) {
                    Timber.e("Failed to update data on auth change", e)
                }
            }
        }
    }

    private fun listenData() {
        listeningJob?.cancel()
        listeningJob = viewModelScope.launch {
            launch { listenRemoteConversationsUseCase.start() }
            launch { listenRemoteUserUseCase.start() }
            launch { listenBlockedUserEvents.start() }
        }
    }

    private suspend fun stopListenData() {
        listenRemoteMessagesUseCase.stopAll()
        listeningJob?.cancel()
    }
}