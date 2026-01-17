package hu.mokegyesulet.it.dunestrat.backend.entities

import hu.mokegyesulet.it.dunestrat.model.PlayerStep
import kotlinx.serialization.json.Json

class PlayerStepDatabaseEntry(
    val id: Int?,
    val gameStateId: Int,
    val json: String,
) {

    fun toPlayerStep(): PlayerStep {
        val playerStep = Json.decodeFromString<PlayerStep>(json)
        return playerStep.copy(id = id, gameStateId = gameStateId)

//        val data = Json.parseToJsonElement(json).jsonObject
//        PlayerStep(
//            id,
//            gameStateId,
//            data["playerId"]!!.toString().removeSurrounding("\""),
//            data["leaveFields"]!!.jsonArray.map {
//                Json.decodeFromString<GameStateField>(it.toString())
//            }.toSet(),
//            data ["enterFields"]!!.jsonArray.map {
//                Json.decodeFromString<GameStateField>(it.toString())
//            }.toSet(),
//            data["purchaseWeapons"]!!.jsonObject.map {
//                val weapon = Json.decodeFromString<hu.mokegyesulet.it.dunestrat.model.Weapon>(
//                    it.key,
//                )
//                val amount = it.value.toString().toInt()
//                weapon to amount
//            }.toMap(),
//            data["buildHarvesters"]!!.jsonArray.map {
//                Json.decodeFromString<GameStateField>(it.toString())
//            }.toSet(),
//        )
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
