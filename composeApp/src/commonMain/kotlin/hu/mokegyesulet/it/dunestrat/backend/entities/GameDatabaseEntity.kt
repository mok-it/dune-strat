package hu.mokegyesulet.it.dunestrat.backend.entities

import hu.mokegyesulet.it.dunestrat.model.Game
import hu.mokegyesulet.it.dunestrat.model.GameProgress
import kotlinx.serialization.json.Json

class GameDatabaseEntry(
    val id: Int?,
    val progress: Int,
    val json: String,
) {
    fun toGame(): Game {
        val game = Json.decodeFromString<Game>(json)
        return game.copy(id = id, progress = GameProgress.entries[progress])
    }

    companion object {
        const val TABLE_NAME = "game"
    }
}

fun Game.toDatabaseEntry(): GameDatabaseEntry = GameDatabaseEntry(
    id = id,
    this.progress.ordinal,
    json = Json.encodeToString(this),
)
