package hu.mokegyesulet.it.dunestrat.backend.entities

import hu.mokegyesulet.it.dunestrat.model.GameState
import hu.mokegyesulet.it.dunestrat.model.GameStateField
import hu.mokegyesulet.it.dunestrat.model.Player
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
    @SerialName("player_jsons")
    val playersJsons: List<String>,
) {
    fun toGameState(): GameState {
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

        val players = playersJsons.map { playerJson ->
            val playerWithFieldIdList = Json.decodeFromString<PlayerWithFieldIdList>(playerJson)
            val ownedFields = playerWithFieldIdList.ownedFieldsId.map { fieldId ->
                fields[fieldId]!!
            }
            Player(
                id = playerWithFieldIdList.id,
                water = playerWithFieldIdList.water,
                spice = playerWithFieldIdList.spice,
                harvestersPurchased = playerWithFieldIdList.harvesterPurchased,
                weapons = playerWithFieldIdList.weapons.toMutableMap(),
                ownedFields = ownedFields.toMutableSet(),
            )
        }

        return GameState(
            id = id,
            gameId = gameId,
            index = index,
            fields = fields.values.toSet(),
            players = players.toSet(),
        )
    }

    companion object {
        const val TABLE_NAME = "game_state"
    }
}

fun GameState.toDatabaseEntity(): GameStateDatabaseEntity = GameStateDatabaseEntity(
    id = id,
    gameId = gameId,
    index = index,
    fieldJsons = fields.map { Json.encodeToString(it.toGameStateFieldWithIdList()) },
    playersJsons = players.map { Json.encodeToString(it.toPlayerWithFieldIdList()) },
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

@Serializable
class PlayerWithFieldIdList(
    val id: Int,
    val water: Int,
    val spice: Int,
    val harvesterPurchased: Int,
    val weapons: Map<Weapon, Int>,
    val ownedFieldsId: Set<String>,
) {
    fun toPlayer() = Player(
        id = id,
        water = water,
        spice = spice,
        harvestersPurchased = harvesterPurchased,
        weapons = weapons.toMutableMap(),
        ownedFields = mutableSetOf(),
    )
}

fun Player.toPlayerWithFieldIdList() = PlayerWithFieldIdList(
    id = id,
    water = water,
    spice = spice,
    harvesterPurchased = harvestersPurchased,
    weapons = weapons.toMap(),
    ownedFieldsId = ownedFields.map { it.id }.toSet(),
)

@Serializable
class GameStateSaveDatabaseEntity(
    @SerialName("game_id")
    val gameId: Int,
    val index: Int,
    @SerialName("field_jsons")
    val fieldJsons: List<String>,
    @SerialName("player_jsons")
    val playersJsons: List<String>,
    @SerialName("player_ids")
    val playersIds: List<Int>,
) {
    constructor(gameStateDatabaseEntity: GameStateDatabaseEntity, playersIds: List<Int>) : this(
        gameId = gameStateDatabaseEntity.gameId,
        index = gameStateDatabaseEntity.index,
        fieldJsons = gameStateDatabaseEntity.fieldJsons,
        playersJsons = gameStateDatabaseEntity.playersJsons,
        playersIds = playersIds,
    )
}
