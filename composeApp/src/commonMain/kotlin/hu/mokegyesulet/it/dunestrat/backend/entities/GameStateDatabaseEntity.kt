package hu.mokegyesulet.it.dunestrat.backend.entities

import hu.mokegyesulet.it.dunestrat.model.GameState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
class GameStateDatabaseEntity(
    val id: Int?,
    @SerialName("game_id")
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

fun GameState.toDatabaseEntry(): GameStateDatabaseEntity = GameStateDatabaseEntity(
    id = id,
    gameId = gameId,
    index = index,
    json = Json.encodeToString(this),
)
