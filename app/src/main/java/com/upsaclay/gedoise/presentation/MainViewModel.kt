
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.gedoise.domain.usecase.ClearDataUseCase
import com.upsaclay.gedoise.domain.usecase.ListenRemoteDataUseCase
import com.upsaclay.gedoise.domain.usecase.SynchronizeDataUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val listenRemoteDataUseCase: ListenRemoteDataUseCase,
    private val synchronizeDataUseCase: SynchronizeDataUseCase,
    private val clearDataUseCase:  ClearDataUseCase
): ViewModel() {
    fun updateDataOnAuthChange() {
        viewModelScope.launch {
            authenticationRepository.authenticated.collectLatest { authenticated ->
                if (authenticated) {
                    listenRemoteDataUseCase.start()
                    synchronizeDataUseCase.synchronize()
                } else {
                    listenRemoteDataUseCase.stop()
                    delay(2000)
                    clearDataUseCase()
                }
            }
        }
    }
}