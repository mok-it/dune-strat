package hu.mokegyesulet.it.dunestrat.feature.mainmenu

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.mokegyesulet.it.dunestrat.backend.AuthStatus
import hu.mokegyesulet.it.dunestrat.backend.SupabaseAuth
import hu.mokegyesulet.it.dunestrat.backend.SupabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainMenuViewModel : ViewModel() {

    val deserts = SupabaseRepository.getDeserts()

    fun onLogin() {
        viewModelScope.launch {
            SupabaseAuth.logIn()
        }
    }

    fun onLogout() {
        viewModelScope.launch {
            SupabaseAuth.logOut()
        }
    }

    val isLoggedIn: Flow<Boolean>
        get() = SupabaseAuth.authStatus.map {
            it is AuthStatus.Authenticated
        }

    val gameCount = mutableStateOf(0)
    val games = SupabaseRepository.getGames()
    sealed class Event() {
        data object CreateGame : Event()
    }
    fun onEvent(event: Event) {
        when (event) {
            is Event.CreateGame -> gameCount.value += 1
        }
    }
}
