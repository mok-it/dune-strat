package hu.mokegyesulet.it.dunestrat.feature.mainmenu

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.mokegyesulet.it.dunestrat.backend.AuthStatus
import hu.mokegyesulet.it.dunestrat.backend.SupabaseAuth
import hu.mokegyesulet.it.dunestrat.backend.SupabaseRepository
import hu.mokegyesulet.it.dunestrat.model.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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

    val gameCount = mutableStateOf(0)
    val games = SupabaseRepository.getGames().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList(),
    )
    val isExpanded = mutableStateOf(false)

//    val waterAmount = SupabaseRepository.getLatestGameStateByGameId().stateIn(
//        viewModelScope,
//        SharingStarted.Eagerly,
//        initialValue = emptyList(),
//    )
    val deserts = SupabaseRepository.getDeserts().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList(),
    )
    val selectedGame = MutableStateFlow<Game?>(null)

    sealed class Event() {
        data object CreateGame : Event()
        data object ExpandMenu : Event()
        data class SelectGame(val game: Game) : Event()
    }
    fun onEvent(event: Event) {
        when (event) {
            is Event.CreateGame -> gameCount.value += 1
            is Event.ExpandMenu -> isExpanded.value = !isExpanded.value
            is Event.SelectGame -> selectedGame.value = event.game
        }
    }
}
