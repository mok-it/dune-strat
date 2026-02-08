package hu.mokegyesulet.it.dunestrat.backend.entities

import hu.mokegyesulet.it.dunestrat.model.GameState
import hu.mokegyesulet.it.dunestrat.model.GameStateField
import hu.mokegyesulet.it.dunestrat.model.Weapon
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
class GameStateDatabaseEntity(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val id: Int = -1,
    @SerialName("game_id")
    val gameId: Int,
    val index: Int,
    @SerialName("field_jsons")
    val fieldJsons: List<String>,
    val json: String,
) {
    fun toGameState(): GameState {
        val gameState = Json.decodeFromString<GameState>(json)

        val fieldsWithIdList = fieldJsons.map { fieldJson ->
            Json.decodeFromString<GameStateFieldWithIdList>(fieldJson)
        }

        val fields = mutableMapOf<String, GameStateField>()

        fieldsWithIdList.forEach { fieldWithIdList ->
            fields[fieldWithIdList.id] = fieldWithIdList.toGameStateField()
        }

        fieldsWithIdList.forEach { fieldWithIdList ->
            val field = fields[fieldWithIdList.id]!!
            fieldWithIdList.neighboursId.forEach { neighbourId ->
                val neighbourField = fields[neighbourId]!!
                field.neighbours.add(neighbourField)
            }
        }

        return gameState.copy(
            id = id,
            gameId = gameId,
            index = index,
            fields = fields.values.toSet(),
        )
    }
    companion object {
        const val TABLE_NAME = "game_state"
    }
}

fun GameState.toDatabaseEntry(): GameStateDatabaseEntity = GameStateDatabaseEntity(
    id = id,
    gameId = gameId,
    index = index,
    fieldJsons = fields.map { Json.encodeToString(it.toGameStateFieldWithIdList()) },
    json = Json.encodeToString(this),
)

@Serializable
class GameStateFieldWithIdList(
    val id: String,
    val water: Int,
    val spice: Int,
    val effectiveWeapon: Weapon,
    val harvester: Boolean,
    val neighboursId: List<String>,
) {
    fun toGameStateField() = GameStateField(
        id = id,
        water = water,
        spice = spice,
        effectiveWeapon = effectiveWeapon,
        harvester = harvester,
        neighbours = mutableSetOf(),
    )
}

fun GameStateField.toGameStateFieldWithIdList() = GameStateFieldWithIdList(
    id = id,
    water = water,
    spice = spice,
    effectiveWeapon = effectiveWeapon,
    harvester = harvester,
    neighboursId = neighbours.map { it.id },
)
