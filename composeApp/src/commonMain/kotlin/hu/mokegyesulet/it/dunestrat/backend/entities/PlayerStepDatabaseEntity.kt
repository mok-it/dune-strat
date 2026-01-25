package hu.mokegyesulet.it.dunestrat.backend.entities

import hu.mokegyesulet.it.dunestrat.model.PlayerStep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
class PlayerStepDatabaseEntry(
    val id: Int?,
    @SerialName("game_state_id")
    val gameStateId: Int,
    val json: String,
) {

    fun toPlayerStep(): PlayerStep {
        val playerStep = Json.decodeFromString<PlayerStep>(json)
        return playerStep.copy(id = id, gameStateId = gameStateId)
    }

    companion object {
        const val TABLE_NAME = "player_step"
    }
}

fun PlayerStep.toDatabaseEntry(): PlayerStepDatabaseEntry = PlayerStepDatabaseEntry(
    id = id,
    gameStateId = gameStateId,
    json = Json.encodeToString(this),
)
