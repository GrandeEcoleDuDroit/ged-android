
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.domain.usecase.ListenAuthenticationStateUseCase
import com.upsaclay.gedoise.domain.usecase.ClearDataUseCase
import com.upsaclay.gedoise.domain.usecase.ListenDataUseCase
import com.upsaclay.gedoise.domain.usecase.SynchronizeDataUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val listenAuthenticationStateUseCase: ListenAuthenticationStateUseCase,
    private val listenDataUseCase: ListenDataUseCase,
    private val synchronizeDataUseCase: SynchronizeDataUseCase,
    private val clearDataUseCase:  ClearDataUseCase
): ViewModel() {
    fun updateDataOnAuthChange() {
        viewModelScope.launch {
            listenAuthenticationStateUseCase.authenticated.collectLatest { authenticated ->
                if (authenticated) {
                    listenDataUseCase.start()
                    synchronizeDataUseCase()
                } else {
                    listenDataUseCase.stop()
                    delay(2000)
                    clearDataUseCase()
                }
            }
        }
    }
}