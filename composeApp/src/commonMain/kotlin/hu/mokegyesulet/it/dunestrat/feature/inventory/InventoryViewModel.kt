package hu.mokegyesulet.it.dunestrat.feature.inventory

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.mokegyesulet.it.dunestrat.backend.SupabaseRepository
import hu.mokegyesulet.it.dunestrat.model.GameState
import hu.mokegyesulet.it.dunestrat.model.Player
import hu.mokegyesulet.it.dunestrat.util.StringNormalizer
import kotlinx.coroutines.launch

class InventoryViewModel(
    val gameId: Int,
) : ViewModel() {

    val gameState: MutableState<GameState?> = mutableStateOf(null)
    val players = mutableStateOf<List<Player>>(emptyList())
    val gameName = mutableStateOf("")

    private lateinit var normalizedGameName: String

    init {
        viewModelScope.launch {
            try {
                val game = SupabaseRepository.getGameById(gameId)
                gameName.value = game.name
                normalizedGameName = StringNormalizer.normalize(game.name)
                val latest = SupabaseRepository.getLatestGameStateByGameId(gameId)
                gameState.value = latest
                players.value = latest.players.toList().sortedBy { it.id }
            } catch (e: Exception) {
                println("HIBA: nem sikerült betölteni a játék állapotát: ${e.message}")
                players.value = emptyList()
            }
        }
    }

    val svgDownloadUrl: String?
        get() {
            return if (gameState.value == null) {
                null
            } else {
                val round = gameState.value!!.index
                val roundString = if (round > 9) round.toString() else "0$round"
                "https://lnwvuwepwaexwybselsf.supabase.co/storage/v1/object/public/svgs/$normalizedGameName($gameId)/$roundString.svg"
            }
        }
}
