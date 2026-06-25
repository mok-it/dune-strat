package hu.mokegyesulet.it.dunestrat.backend.entities

import hu.mokegyesulet.it.dunestrat.model.Game
import hu.mokegyesulet.it.dunestrat.model.GameProgress
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
class GameDatabaseEntity(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val id: Int = -1,
    val progress: Int,
    @SerialName("desert_id")
    val desertId: Int,
    val json: String,
) {
    fun toGame(): Game {
        val game = Json.decodeFromString<Game>(json)
        return game.copy(id = id, progress = GameProgress.entries[progress], desertId = desertId)
    }

    companion object {
        const val TABLE_NAME = "game"
    }
}

fun Game.toDatabaseEntity(): GameDatabaseEntity = GameDatabaseEntity(
    id = id,
    this.progress.ordinal,
    json = Json.encodeToString(this),
    desertId = desertId,
)
