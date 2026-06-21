package hu.mokegyesulet.it.dunestrat.inventory

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.mokegyesulet.it.dunestrat.backend.SupabaseRepository
import hu.mokegyesulet.it.dunestrat.model.Player
import hu.mokegyesulet.it.dunestrat.model.Weapon
import kotlinx.coroutines.launch

class InventoryViewModel(
    val gameId: Int,
) : ViewModel() {

    val players = mutableStateOf<List<Player>>(emptyList())

    init {
        viewModelScope.launch {
            try {
                val latest = SupabaseRepository.getLatestGameStateByGameId(gameId)
                players.value = latest.players.toList().sortedBy { it.id }
            } catch (e: Exception) {
                println("HIBA: nem sikerült betölteni a játék állapotát: ${e.message}")
                players.value = emptyList()
            }
        }
    }
}
