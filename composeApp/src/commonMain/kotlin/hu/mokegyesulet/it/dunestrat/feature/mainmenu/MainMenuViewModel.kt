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
import hu.mokegyesulet.it.dunestrat.util.StringNormalizer.normalize
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainMenuViewModel : ViewModel() {

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

    val svgDownloadUrl: String?
        get() {
            return if (latestGameState.value == null) {
                null
            } else {
                val name = normalize(selectedGame.value!!.name)
                val round = latestGameState.value!!.index
                val roundString = if (round > 9) round.toString() else "0$round"
                "https://lnwvuwepwaexwybselsf.supabase.co/storage/v1/object/public/svgs/$name(${selectedGame.value!!.id})/$roundString.svg"
            }
        }
    sealed class Event {
        data class SelectGame(val game: Game) : Event()
        data object UnSelectGame : Event()
    }
    fun onEvent(event: Event) {
        when (event) {
            is Event.SelectGame -> {
                if (selectedGame.value != event.game) {
                    selectedGame.value = event.game
                    latestGameState.value = null
                    viewModelScope.launch {
                        latestGameState.value =
                            SupabaseRepository.getLatestGameStateByGameId(event.game.id)
                    }
                } else {
                    selectedGame.value = null
                    latestGameState.value = null
                }
            }

            Event.UnSelectGame -> {
                selectedGame.value = null
                latestGameState.value = null
            }
        }
    }
}
