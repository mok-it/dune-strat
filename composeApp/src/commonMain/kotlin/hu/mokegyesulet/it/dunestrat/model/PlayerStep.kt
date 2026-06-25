package hu.mokegyesulet.it.dunestrat.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PlayerStep(
    @Transient
    val id: Int = -1,
    @Transient
    val gameStateId: Int = -1,
    @Transient
    val playerId: Int = -1,
    var leaveFields: Set<String> = emptySet(),
    var enterFields: Set<String> = emptySet(),
    var purchaseWeapons: Map<Weapon, Int> = emptyMap(),
    var buildHarvesters: Set<String> = emptySet(),
)
