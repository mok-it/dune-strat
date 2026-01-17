package hu.mokegyesulet.it.dunestrat.feature.mainmenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.mokegyesulet.it.dunestrat.backend.SupabaseAuth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
        get() = SupabaseAuth.sessionStatus.map {
            it is SessionStatus.Authenticated
        }
}
