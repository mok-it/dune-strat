package hu.mokegyesulet.it.dunestrat.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PlayerStep(
    @Transient
    val id: Int = -1,
    @Transient
    val gameStateId: Int = -1,
    val playerId: String,
    var leaveFields: Set<GameStateField>,
    var enterFields: Set<GameStateField>,
    var purchaseWeapons: Map<Weapon, Int>,
    var buildHarvesters: Set<GameStateField>,
)
