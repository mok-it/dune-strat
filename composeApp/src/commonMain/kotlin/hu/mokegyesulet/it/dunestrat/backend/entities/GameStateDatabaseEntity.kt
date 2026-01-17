package hu.mokegyesulet.it.dunestrat.backend.entities

import hu.mokegyesulet.it.dunestrat.model.GameState
import kotlinx.serialization.json.Json

class GameStateDatabaseEntry(
    val id: Int?,
    val gameId: Int,
    val index: Int,
    val json: String,
) {
    fun toGameState(): GameState {
        val gameState = Json.decodeFromString<GameState>(json)
        return gameState.copy(id = id, gameId = gameId, index = index)
    }
    companion object {
        const val TABLE_NAME = "game_state"
    }
}

fun GameState.toDatabaseEntry(): GameStateDatabaseEntry = GameStateDatabaseEntry(
    id = id,
    gameId = gameId,
    index = index,
    json = Json.encodeToString(this),
)
