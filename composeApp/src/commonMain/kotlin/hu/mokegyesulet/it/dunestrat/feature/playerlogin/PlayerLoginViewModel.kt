package hu.mokegyesulet.it.dunestrat.feature.playerlogin

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class PlayerLoginViewModel : ViewModel() {
    val code = mutableStateOf("")
    val enabled = mutableStateOf(false)
    fun onEvent(event: PlayerLoginEvent) {
        when (event) {
            is PlayerLoginEvent.CodeChanged -> {
                code.value = event.newCode
            }
        }
        enabled.value = code.value.isNotEmpty()
    }
    sealed class PlayerLoginEvent {
        data class CodeChanged(val newCode: String) : PlayerLoginEvent()
    }
}
