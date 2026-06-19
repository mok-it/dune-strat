package hu.mokegyesulet.it.dunestrat.feature.mainmenu

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.mokegyesulet.it.dunestrat.backend.AuthStatus
import hu.mokegyesulet.it.dunestrat.backend.SupabaseAuth
import hu.mokegyesulet.it.dunestrat.backend.SupabaseRepository
import hu.mokegyesulet.it.dunestrat.model.Game
import hu.mokegyesulet.it.dunestrat.model.GameState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    val games = SupabaseRepository.getGames().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList(),
    )

    val selectedGame = mutableStateOf<Game?>(null)

    val latestGameState = mutableStateOf<GameState?>(null)

    val players = derivedStateOf {
        latestGameState.value?.players ?: emptySet()
    }

    sealed class Event {
        data class SelectGame(val game: Game) : Event()
    }
    fun onEvent(event: Event) {
        when (event) {
            is Event.SelectGame -> {
                if (selectedGame.value != event.game) {
                    selectedGame.value = event.game
                    viewModelScope.launch {
                        latestGameState.value = SupabaseRepository.getLatestGameStateByGameId(event.game.id)
                    }
                } else {
                    selectedGame.value = null
                    latestGameState.value = null
                }
            }
        }
    }
}
