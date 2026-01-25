package hu.mokegyesulet.it.dunestrat.backend.entities

import hu.mokegyesulet.it.dunestrat.model.Desert
import kotlinx.serialization.json.Json

class DesertDatabaseEntity(
    val id: Int?,
    val json: String,
) {
    fun toDesert(): Desert {
        val desert = Json.decodeFromString<Desert>(json)
        return desert.copy(id = id)
    }

    companion object {
        const val TABLE_NAME = "desert"
    }
}

fun Desert.toDatabaseEntity() = DesertDatabaseEntity(
    id = id,
    json = Json.encodeToString(this),
)
