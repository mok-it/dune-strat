package hu.mokegyesulet.it.dunestrat.backend.entities

import hu.mokegyesulet.it.dunestrat.model.Desert
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
class DesertDatabaseEntity(
    val id: Int?,
    val name: String,
    val json: String,
) {
    fun toDesert(): Desert {
        val desert = Json.decodeFromString<Desert>(json)
        return desert.copy(id = id, name = name)
    }

    companion object {
        const val TABLE_NAME = "desert"
    }
}

fun Desert.toDatabaseEntity() = DesertDatabaseEntity(
    id = id,
    name = name,
    json = Json.encodeToString(this),
)
